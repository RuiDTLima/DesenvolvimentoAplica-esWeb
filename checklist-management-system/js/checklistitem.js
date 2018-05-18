import React, {Component} from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import {request} from './request'

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

      request(
        this.props.url + action.href,
        action.method,
        this.props.credentials,
        action.type,
        JSON.stringify(params),
        (resp) => {
          this.setState(old => ({display: true}))
          if (action.method === 'DELETE') this.props.onDelete()
        },
        (err) => {
          console.log(err)
        }
      )
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
