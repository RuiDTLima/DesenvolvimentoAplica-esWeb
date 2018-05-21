import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import errorHandler from '../errorHandler'
import presentError from '../presentError'
import actionRequest from '../actionRequest'
import viewFromAction from '../viewFromAction'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = { }
    this.display = this.display.bind(this)
  }

  componentWillUnmount () {
    if (this.promise) {
      this.promise.cancel()
    }
  }

  render () {
    if (this.state.error) {
      return presentError(this.state.error, () => this.setState({error: undefined}))
    }
    const action = this.state.action
    if (action) {
      if (!action.fields) {
        actionRequest(
          this.props.url + action.href,
          action,
          this.props.credentials,
          null,
          (resp) => {
            this.promise = undefined
            if (action.method === 'DELETE') this.props.onReturn()
            else this.setState(old => ({action: undefined}))
            return resp
          },
          (err) => {
            this.promise = undefined
            this.setState({error: err})
          }
        )
        return this.display()
      }
      return viewFromAction(
        action,
        this.props.actionGenerator,
        (name) => document.getElementById(name).value,
        (body) => actionRequest(
          this.props.url + action.href,
          action,
          this.props.credentials,
          body,
          (resp) => {
            this.promise = undefined
            if (action.method === 'DELETE') this.props.onReturn()
            else this.setState(old => ({action: undefined}))
            return resp
          },
          (err) => {
            this.promise = undefined
            this.setState({error: err})
          }
        ),
        () => this.setState(old => ({action: undefined}))
      )
    }

    return this.display()
  }

  /**
   * Requests and calls onSuccess over the checklist obtained.
   * @param {function} onSuccess
   */
  getChecklist (onSuccess) {
    return <div>
      <HttpGet // /checklist/{id}
        url={this.props.url + this.props.partial}
        credentials={this.props.credentials}
        render={(checklistResult => (
          <HttpGetSwitch result={checklistResult}
            onError={err => {
              return errorHandler(err, 'Checklist not found.', () => this.setState({error: undefined}))
            }}
            onJson={checklist => {
              return onSuccess(checklist)
            }}
          />
        ))} />
    </div>
  }

  /**
   * Requests and calls onSuccess over the list of checklist items corresponding
   * to the given checklist.
   * @param {object} checklist
   * @param {function} onSuccess
   */
  getChecklistItems (checklist, onSuccess) {
    return <div>
      <HttpGet // /checklist/{id}/checklistitems
        url={this.props.url + checklist.entities.find(e => e.class.includes('checklistitem')).href}
        credentials={this.props.credentials}
        render={checklistItemsResult => (
          <HttpGetSwitch result={checklistItemsResult}
            onError={err => {
              return errorHandler(err, 'Checklist Items not found.', () => this.setState({error: undefined}))
            }}
            onJson={checklistItems => {
              return onSuccess(checklistItems)
            }}
          />
        )} />
    </div>
  }

  /**
   * Requests and calls onSuccess over the specific checklist item obtained.
   * @param {object} checklistItems
   * @param {function} onSuccess
   */
  getChecklistItem (checklistItems, onSuccess) {
    const firstElement = checklistItems.collection.items[0]
    if (!firstElement) return new Error('') // ERROR
    const lastIndex = firstElement.href.lastIndexOf('/')

    return <div>
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
                return onSuccess(checklistItem)
              }}
            />
          )}
      />
    </div>
  }

  /**
   * Central view of every information regarding a specific checklist item.
   */
  display () {
    return this.getChecklist(checklist => {
      return this.getChecklistItems(checklist, (checklistItems) => {
        return this.getChecklistItem(checklistItems, (checklistItem) => {
          return <div>
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
        })
      })
    })
  }
}
