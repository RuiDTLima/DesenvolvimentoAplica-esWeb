import React, {Component} from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import fetch from 'isomorphic-fetch'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      method: ''
    }
  }

  render () {
    const path = this.props.baseUrl + this.state.href
    if (this.state.method !== '') {
      if (this.state.fields === undefined) {
        fetch(path, {
          method: this.state.method,
          headers: {
            'Authorization': this.props.credentials
          }
        }).then(resp => {
          if (resp.status === 204) {
            this.props.history.push(`/checklisttemplates/${this.state.template_id}`)
          }
        })
      } else {
        return this.props.actionsForm(path, this.state.method, this.state.fields, (ev) => {
          ev.preventDefault()
          const obj = { }
          this.state.fields.forEach(d => {
            obj[d.name] = (document.getElementsByName(d.name)[0].value)
          })

          fetch(path, {
            method: this.state.method,
            headers: {
              'Authorization': this.props.credentials,
              'content-type': this.state.type
            },
            body: JSON.stringify(obj)
          }).then(resp => {
            if (resp.status === 204) {
              this.setState(() => ({method: ''}))
            }
          })
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
                    onClick={() => this.setState({method: actions.method, href: actions.href, type: actions.type, fields: actions.fields, template_id: json.properties['checklisttemplate_id']})}>
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
