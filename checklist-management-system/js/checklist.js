import React, {Component} from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import ChecklistItems from './checklistitems'
import fetch from 'isomorphic-fetch'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      display: true
    }
    this.display = this.display.bind(this)
    this.viewFromAction = this.viewFromAction.bind(this)
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

      // TODO SEPARATE
      if (action.type === 'application/x-www-form-urlencoded') {
        const formData = new FormData()
        params.keys.forEach(k => formData.append(k, params[k]))
        params = formData
      } else params = JSON.stringify(params)

      fetch(this.props.url + action.href, {
        method: action.method,
        headers: {
          'Authorization': this.props.credentials,
          'content-type': action.type
        },
        body: params
      })
        .then(resp => {
          if (resp.status === 204) {
            this.setState(old => ({display: true}))
            if (action.method === 'DELETE') this.props.onDelete()
          } else if (resp.status >= 400 && resp.status < 500) {
            return new Error('error')
          } else return new Error('server error')
        })
    }, () => this.setState(old => ({display: true})))
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
                            'display': false,
                            'action': action
                          }))}>
                            {action.title}
                          </button>
                        )
                      }
                      <table>
                        <tbody>
                          {Object.keys(json.properties).map(item =>
                            <tr key={item}>
                              <th>{item}</th>
                              <td>{json.properties[item]}</td>
                            </tr>
                          )}
                        </tbody>
                      </table>
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
    if (this.state.display) {
      return this.display()
    }

    return this.viewFromAction()
  }
}
