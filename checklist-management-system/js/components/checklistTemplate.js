import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import errorHandler from '../errorHandler'
import presentError from '../presentError'
import TemplateItems from './templateItems'
import actionRequest from '../actionRequest'
import viewFromAction from '../viewFromAction'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      action: undefined
    }
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
      if (action.fields === undefined) {
        actionRequest(
          this.props.baseUrl + action.href,
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
          this.props.baseUrl + action.href,
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
   * Central view of every information regarding the list of checklists.
   */
  display () {
    return <div>
      <HttpGet
        url={this.props.baseUrl + this.props.specificUrl}
        credentials={this.props.credentials}
        render={(result => (
          <HttpGetSwitch result={result} // onError handler
            onError={err => {
              return errorHandler(err, 'Checklist Template not found.', () => this.setState({error: undefined}))
            }}
            onJson={json => {
              if (json.class[0] === 'checklisttemplate') { // Error
                let btn
                if (json.properties['usable']) {
                  btn = <div>{json.actions.map(actions =>
                    <button key={actions.name}
                      onClick={() => this.setState({action: actions})}>
                      {actions.title}
                    </button>
                  )}
                  </div>
                }
                return <div>
                  {btn}
                  <button key='back' onClick={() => this.props.onReturn()}>Back</button>
                  <ul>
                    <li><b>Id:</b> {json.properties['checklisttemplate_id']}</li>
                    <li><b>Name:</b> {json.properties['name']}</li>
                    <li><b>Usable:</b> {json.properties['usable'].toString()}</li>
                  </ul>
                  <TemplateItems
                    baseUrl={this.props.baseUrl}
                    partialUrl={json.entities[0].href}
                    credentials={this.props.credentials}
                    templateId={json.properties['checklisttemplate_id']}
                    onClick={(url) => this.props.onClick(url)}
                  />
                </div>
              }
            }}
          />
        ))}
      />
    </div>
  }
}
