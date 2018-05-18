import React, {Component} from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import {request} from './request'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = { }
    this.display = this.display.bind(this)
    this.viewFromAction = this.viewFromAction.bind(this)
    this.actionRequest = this.actionRequest.bind(this)
  }

  actionRequest (action, body) {
    return request(
      this.props.url + action.href,
      action.method,
      this.props.credentials,
      action.type,
      JSON.stringify(body),
      (resp) => {
        if (action.method === 'DELETE') this.props.onDelete()
        else this.setState(old => ({action: undefined}))
      },
      (err) => {
        console.log(err)
      }
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

      request(
        this.props.url + action.href,
        action.method,
        this.props.credentials,
        action.type,
        JSON.stringify(params),
        (resp) => {
          if (action.method === 'DELETE') this.props.onDelete()
          else this.setState(old => ({action: undefined}))
        },
        (err) => {
          console.log(err)
        }
      )
    }, () => this.setState(old => ({action: undefined})))
  }

  display () {
    return (
      <div>
        <HttpGet // /checklist/{id}
          url={this.props.url + this.props.partial}
          credentials={this.props.credentials}
          render={(checklistResult => (
            <HttpGetSwitch result={checklistResult}
              onJson={checklist => {
                return (
                  <HttpGet // /checklist/{id}/checklistitems
                    url={this.props.url + checklist.entities.find(e => e.class.includes('checklistitem')).href}
                    credentials={this.props.credentials}
                    render={checklistItemsResult => (
                      <HttpGetSwitch result={checklistItemsResult}
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

  render () {
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
}
