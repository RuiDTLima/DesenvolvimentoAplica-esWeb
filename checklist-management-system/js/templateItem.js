import React, {Component} from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import {request} from './request'

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
            this.props.history.push(`/checklisttemplates/${this.state.template_id}`)
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
        })
      }
    }

    return (
      <div>
        <HttpGet
          url={this.props.baseUrl + this.props.specificUrl}
          credentials={this.props.credentials}
          render={(result => (
            <HttpGetSwitch result={result}
              onJson={json => {
                let btn = <div>{json.actions.map(actions =>
                  <button key={actions.name}
                    onClick={() => this.setState({action: actions, template_id: json.properties['checklisttemplate_id']})}>
                    {actions.title}
                  </button>
                )}
                </div>
                return (
                  <div>
                    {btn}
                    <ul>
                      <li>{json.properties['templateitem_id']}</li>
                      <li>{json.properties['name']}</li>
                      <li>{json.properties['description']}</li>
                      <li>{json.properties['checklisttemplate_id']}</li>
                    </ul>
                  </div>
                )
              }}
            />
          ))}
        />
      </div>
    )
  }
}
