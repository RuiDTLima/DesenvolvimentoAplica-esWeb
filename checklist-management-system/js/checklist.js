import React, {Component} from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import ChecklistItems from './checklistitems'
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
      this.actionRequest(action, params)
    }, () => this.setState(old => ({action: undefined})))
  }

  display () {
    return (
      <div>
        <HttpGet
          url={this.props.url + this.props.partial}
          credentials={this.props.credentials}
          render={(result) => (
            <div>
              <HttpGetSwitch result={result}
                onJson={json => {
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
                }}
              />
            </div>
          )} />
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
