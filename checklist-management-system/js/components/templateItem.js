import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import {request} from '../request'
import errorHandler from '../errorHandler'
import presentError from '../presentError'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      action: undefined
    }
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

  viewFromAction () {
    const action = this.state.action
    return this.props.actionGenerator(action, () => {
      const obj = { }
      action.fields.forEach(d => {
        obj[d.name] = (document.getElementsByName(d.name)[0].value)
      })

      this.actionRequest(action, obj)
    }, () => this.setState(old => ({action: undefined})))
  }

  actionRequest (action, body) {
    return request(
      this.props.baseUrl + this.state.action.href,
      this.state.action.method,
      this.props.credentials,
      this.state.action.type,
      JSON.stringify(body),
      (resp) => {
        if (action.method === 'DELETE') return this.props.onClick(`/checklisttemplates/${this.state.template_id}`)
        this.setState(() => ({action: undefined}))
      },
      (err) => {
        this.setState({error: err})
      }
    )
  }
}
