import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import ChecklistItems from './checklistitems'
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
      <HttpGet
        url={this.props.url + this.props.partial}
        credentials={this.props.credentials}
        render={(result) => (
          <HttpGetSwitch result={result}
            onError={err => {
              return errorHandler(err, 'Checklist not found.', () => this.setState({error: undefined}))
            }}
            onJson={json => {
              return onSuccess(json)
            }}
          />
        )}
      />
    </div>
  }

  /**
   * Central view of every information regarding a specific checklist.
   */
  display () {
    return this.getChecklist((json) => {
      return (
        <div>
          {
            json.actions.map((action, index) =>
              <button key={action.name} onClick={() => this.setState(old => ({
                'action': action
              }))}>
                {action.title}
              </button>
            )
          }
          <button key='back' onClick={() => this.props.onReturn()}>Back</button>
          <ul>
            <li><b>Name:</b> {json.properties['name']}</li>
            <li><b>Checklist Id:</b> {json.properties['checklist_id']}</li>
            <li><b>Completion Date:</b> {json.properties['completion_date']}</li>
            <li><b>Completion State:</b> {json.properties['completion_state']}</li>
          </ul>
          {
            json.entities.find(e => e.class.includes('checklisttemplates')) &&
            <button onClick={() => this.props.onSelectTemplate(json.properties.checklisttemplate_id)}>{json.entities.find(e => e.class.includes('checklisttemplates')).class[0]}</button>
          }
          <ChecklistItems
            url={this.props.url}
            partial={json.entities.find(e => e.class[0] === 'checklistitem').href}
            credentials={this.props.credentials}
            onSelectItem={this.props.onSelectItem}
          />
        </div>
      )
    })
  }
}
