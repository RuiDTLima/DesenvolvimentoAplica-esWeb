import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import Paginator from '../paginator'
import {request} from '../request'
import errorHandler from '../errorHandler'
import presentError from '../presentError'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      action: undefined
    }
    this.showTemplate = this.showTemplate.bind(this)
  }

  render () {
    if (this.state.error) {
      return presentError(this.state.error, () => this.setState({error: undefined}))
    }
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
            this.props.onClick('/checklisttemplates')
          },
          (err) => {
            this.setState({error: err})
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
              this.setState({error: err})
            }
          )
        }, () => this.setState(old => ({action: undefined})))
      }
    }

    return (
      <div>
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
                  return this.showTemplate(btn, json, this.props)
                }
              }}
            />
          ))}
        />
      </div>
    )
  }

  showTemplate (btn, json, props) {
    return <div>
      {btn}
      <button key='back' onClick={() => this.props.onReturn()}>Back</button>
      <ul>
        <li><b>Id:</b> {json.properties['checklisttemplate_id']}</li>
        <li><b>Name:</b> {json.properties['name']}</li>
        <li><b>Usable:</b> {json.properties['usable'].toString()}</li>
      </ul>
      <HttpGet
        url={props.baseUrl + json.entities[0].href}
        credentials={props.credentials}
        template={json}
        render={(result => (
          <HttpGetSwitch result={result}
            onError={err => {
              errorHandler(err, 'Checklist Template Items not found.', () => this.setState({error: undefined}))
            }}
            onJson={items => {
              return (
                <div>
                  {items.collection.items.length !== 0 ? (<h3>Template Items</h3>) : ''}

                  <Paginator response={items} onChange={nUrl => result.setUrl(props.baseUrl + nUrl)} />
                  <ul>
                    {items.collection.items.map(item =>
                      <li key={item.data.find(d => d.name === 'templateitem_id').value}>
                        <button onClick={() => {
                          props.onClick(`/checklisttemplates/${json.properties['checklisttemplate_id']}/templateitems/${item.data.find(d => d.name === 'templateitem_id').value}`)
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
}
