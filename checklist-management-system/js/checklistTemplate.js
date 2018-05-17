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
            this.props.history.push('/checklisttemplates')
          }
        })
      } else {
        return this.props.actionsForm(path, this.state.method, this.state.fields, (ev) => {
          ev.preventDefault()
          const obj = { }
          this.state.fields.forEach(d => {
            obj[d.name] = (document.getElementsByName(d.name)[0].value)
          })
          console.log(obj)

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
            <HttpGetSwitch result={result} // onError handler
              onJson={json => {
                if (json.class[0] === 'checklisttemplate') { // Error
                  let btn = <div>{json.actions.map(actions =>
                    <button key={actions.name}
                      onClick={() => this.setState({method: actions.method, href: actions.href, type: actions.type, fields: actions.fields})}>
                      {actions.title}
                    </button>
                  )}
                  </div>
                  return showTemplate(btn, json, this.props)
                }
              }}
            />
          ))}
        />
      </div>
    )
  }
}

function showTemplate (btn, json, props) {
  return <div>
    {btn}
    <ul>
      <li>{json.properties['checklisttemplate_id']}</li>
      <li>{json.properties['name']}</li>
      <li>{json.properties['usable'].toString()}</li>
    </ul>
    <HttpGet
      url={props.baseUrl + json.entities[0].href}
      credentials={props.credentials}
      template={json}
      render={(result => (
        <HttpGetSwitch result={result}
          onJson={items => {
            return (
              <div>
                {items.collection.items.length !== 0 ? (<h3>Template Items</h3>) : ''}

                <ul>
                  {items.collection.items.map(item =>
                    <li key={item.data.find(d => d.name === 'name').value}>
                      <button onClick={() => {
                        props.history.push(item.href)
                      }}>
                        {item.data.find(d => d.name === 'name').value}
                      </button>
                    </li>
                  )}
                </ul>
              </div>
            )
          }}
        />
      ))
      }
    />
  </div>
}
