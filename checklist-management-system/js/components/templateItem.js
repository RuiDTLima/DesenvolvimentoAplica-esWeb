import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import {request} from '../request'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      action: undefined
    }
  }

  render () {
    if (this.state.action) {
      const path = this.props.baseUrl + this.state.action.href
      if (this.state.action.fields === undefined) {
        request(
          path,
          this.state.action.method,
          this.props.credentials,
          'application/json',
          null,
          (resp) => {
            this.props.onClick(`/checklisttemplates/${this.state.template_id}`)
          },
          (err) => {
            console.log(err)
          }
        )
      } else {
        return this.props.actionGenerator(this.state.action, () => {
          const obj = { }
          this.state.action.fields.forEach(d => {
            obj[d.name] = (document.getElementsByName(d.name)[0].value)
          })

          request(
            path,
            this.state.action.method,
            this.props.credentials,
            this.state.action.type,
            JSON.stringify(obj),
            (resp) => {
              this.setState(() => ({action: undefined}))
            },
            (err) => {
              console.log(err)
            }
          )
        }, () => this.setState(old => ({action: undefined})))
      }
    }

    return (
      <div>
        <HttpGet // /checklisttemplate/{id}
          url={this.props.baseUrl + this.props.specificUrl}
          credentials={this.props.credentials}
          render={(templateResult => (
            <HttpGetSwitch result={templateResult}
              onJson={template => {
                console.log('TEMPLAAAATE')
                console.log(template)
                return (
                  <HttpGet // /checklisttemplate/{id}/templateitems
                    url={this.props.baseUrl + template.entities.find(e => e.class.includes('templateItem')).href}
                    credentials={this.props.credentials}
                    render={templateItemsResult => (
                      <HttpGetSwitch result={templateItemsResult}
                        onJson={templateItems => {
                          console.log('ITEEEEEMS')
                          console.log(templateItems)
                          const firstElement = templateItems.collection.items[0]
                          if (!firstElement) return new Error('') // ERROR
                          const lastIndex = firstElement.href.lastIndexOf('/')
                          return (
                            <HttpGet // /checklisttemplate/{id}/templateitems/{itemId}
                              url={`${this.props.baseUrl + firstElement.href.substring(0, lastIndex)}/${this.props.itemId}`}
                              credentials={this.props.credentials}
                              render={
                                templateItemResult => (
                                  <HttpGetSwitch result={templateItemResult}
                                    onJson={templateItem => {
                                      let btn
                                      console.log('IIIITEEEEM')
                                      console.log(templateItem)
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
                                          <ul>
                                            <li><b>Item Id:</b> {templateItem.properties['templateitem_id']}</li>
                                            <li><b>Name:</b> {templateItem.properties['name']}</li>
                                            <li><b>Description:</b> {templateItem.properties['description']}</li>
                                            <li><b>Template Id:</b> {templateItem.properties['checklisttemplate_id']}</li>
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
}
