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
      if (!action.fields) {
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
   * Requests and calls onSuccess over the checklist template obtained.
   * @param {function} onSuccess
   */
  getChecklistTemplate (onSuccess) {
    return <div>
      <HttpGet // /checklisttemplate/{id}
        url={this.props.baseUrl + this.props.specificUrl}
        credentials={this.props.credentials}
        render={(templateResult => (
          <HttpGetSwitch result={templateResult}
            onError={err => {
              return errorHandler(err, 'Checklist Template not found.', () => this.setState({error: undefined}))
            }}
            onJson={template => {
              return onSuccess(template)
            }}
          />
        ))} />
    </div>
  }

  /**
   * Requests and calls onSuccess over the list of checklist template items corresponding
   * to the given checklist template.
   * @param {object} template
   * @param {function} onSuccess
   */
  getTemplateItems (template, onSuccess) {
    return <div>
      <HttpGet // /checklisttemplate/{id}/templateitems
        url={this.props.baseUrl + template.entities.find(e => e.class.includes('templateItem')).href}
        credentials={this.props.credentials}
        render={templateItemsResult => (
          <HttpGetSwitch result={templateItemsResult}
            onError={err => {
              return errorHandler(err, 'Checklist Template Items not found.', () => this.setState({error: undefined}))
            }}
            onJson={templateItems => {
              return onSuccess(templateItems)
            }}
          />
        )} />
    </div>
  }

  /**
   * Requests and calls onSuccess over the specific checklist template item obtained.
   * @param {object} templateItems
   * @param {function} onSuccess
   */
  getTemplateItem (templateItems, onSuccess) {
    const firstElement = templateItems.collection.items[0]
    if (!firstElement) return new Error('') // ERROR
    const lastIndex = firstElement.href.lastIndexOf('/')
    return <div>
      <HttpGet // /checklisttemplate/{id}/templateitems/{itemId}
        url={`${this.props.baseUrl + firstElement.href.substring(0, lastIndex)}/${this.props.itemId}`}
        credentials={this.props.credentials}
        render={
          templateItemResult => (
            <HttpGetSwitch result={templateItemResult}
              onError={err => {
                return errorHandler(err, 'Checklist Template Item not found.', () => this.setState({error: undefined}))
              }}
              onJson={templateItem => {
                return onSuccess(templateItem)
              }}
            />
          )}
      />
    </div>
  }

  /**
   * Central view of every information regarding a specific checklist template item.
   */
  display () {
    return this.getChecklistTemplate(template => {
      return this.getTemplateItems(template, (templateItems) => {
        return this.getTemplateItem(templateItems, (templateItem) => {
          let btn
          if (template.properties['usable']) {
            btn = <div>{templateItem.actions.map(actions =>
              <button key={actions.name}
                onClick={() => this.setState({action: actions, template_id: templateItem.properties['checklisttemplate_id']})}>
                {actions.title}
              </button>
            )}
            </div>
          }
          return (
            <div>
              {btn}
              <button key='back' onClick={() => this.props.onReturn()}>Back</button>
              <ul>
                <li><b>Item Id:</b> {templateItem.properties['templateitem_id']}</li>
                <li><b>Name:</b> {templateItem.properties['name']}</li>
                <li><b>Description:</b> {templateItem.properties['description']}</li>
                <li><b>Template Id:</b> {templateItem.properties['checklisttemplate_id']}</li>
              </ul>
            </div>
          )
        })
      })
    })
  }
}
