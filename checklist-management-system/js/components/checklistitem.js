import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import {request} from '../request'
import errorHandler from '../errorHandler'
import presentError from '../presentError'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = { }
    this.display = this.display.bind(this)
    this.viewFromAction = this.viewFromAction.bind(this)
    this.actionRequest = this.actionRequest.bind(this)
  }

  render () {
    if (this.state.error) {
      return presentError(this.state.error, () => this.setState({error: undefined}))
    }
    const action = this.state.action
    if (action) {
      if (!action.fields) {
        this.actionRequest(action)
        return this.display()
      }
      return this.viewFromAction()
    }

    return this.display()
  }

  display () {
    return (
      <div>
        <HttpGet // /checklist/{id}
          url={this.props.url + this.props.partial}
          credentials={this.props.credentials}
          render={(checklistResult => (
            <HttpGetSwitch result={checklistResult}
              onError={err => {
                return errorHandler(err, 'Checklist not found.', () => this.setState({error: undefined}))
              }}
              onJson={checklist => {
                return (
                  <HttpGet // /checklist/{id}/checklistitems
                    url={this.props.url + checklist.entities.find(e => e.class.includes('checklistitem')).href}
                    credentials={this.props.credentials}
                    render={checklistItemsResult => (
                      <HttpGetSwitch result={checklistItemsResult}
                        onError={err => {
                          return errorHandler(err, 'Checklist Items not found.', () => this.setState({error: undefined}))
                        }}
                        onJson={checklistItems => {
                          const firstElement = checklistItems.collection.items[0]
                          if (!firstElement) return new Error('') // ERROR
                          const lastIndex = firstElement.href.lastIndexOf('/')
                          return (
                            <HttpGet // /checklist/{id}/checklistitems/{itemId}
                              url={`${this.props.url + firstElement.href.substring(0, lastIndex)}/${this.props.itemId}`}
                              credentials={this.props.credentials}
                              render={
                                checklistItemResult => (
                                  <HttpGetSwitch result={checklistItemResult}
                                    onError={err => {
                                      return errorHandler(err, 'Checklist Item not found.')
                                    }}
                                    onJson={checklistItem => {
                                      return (
                                        <div>
                                          {
                                            checklistItem.actions.map((action, index) =>
                                              <button key={action.name} onClick={() => this.setState(old => ({
                                                'action': action
                                              }))}>
                                                {action.title}
                                              </button>
                                            )
                                          }
                                          <button key='back' onClick={() => this.props.onReturn()}>Back</button>
                                          <ul>
                                            <li><b>Checklist Id:</b> {checklistItem.properties['id']}</li>
                                            <li><b>Name:</b> {checklistItem.properties['name']}</li>
                                            <li><b>Description:</b> {checklistItem.properties['description']}</li>
                                            <li><b>State:</b> {checklistItem.properties['state']}</li>
                                            <li><b>Checklist Id:</b> {checklistItem.properties['checklist_id']}</li>
                                          </ul>
                                        </div>
                                      )
                                    }}
                                  />
                                )
                              }
                            />
                          )
                        }} />
                    )}
                  />
                )
              }} />
          ))}
        />
      </div>
    )
  }

  /**
   * Generates a view based on a siren action
   */
  viewFromAction () {
    const action = this.state.action
    return this.props.actionGenerator(action, () => {
      let params = { }
      action.fields.forEach(f => {
        params[f.name] = document.getElementById(f.name).value
      })

      this.actionRequest(action, params)
    }, () => this.setState(old => ({action: undefined})))
  }

  actionRequest (action, body) {
    return request(
      this.props.url + action.href,
      action.method,
      this.props.credentials,
      action.type,
      JSON.stringify(body),
      (resp) => {
        if (action.method === 'DELETE') this.props.onReturn()
        else this.setState(old => ({action: undefined}))
      },
      (err) => {
        this.setState({error: err})
      }
    )
  }
}
