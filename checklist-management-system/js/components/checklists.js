import React, {Component} from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import Paginator from '../paginator'
import {request} from '../request'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      create: false
    }
    this.presentError = this.presentError.bind(this)
  }

  render () {
    if (this.state.error) {
      return this.presentError(this.state.error)
    }
    if (this.state.create) {
      return this.props.formGenerator(this.state.template, (ev) => {
        ev.preventDefault()
        const obj = { }
        this.state.template.data.forEach(d => { obj[d.name] = (document.getElementsByName(d.name)[0].value) })
        request(
          this.props.url + this.props.partial,
          'POST',
          this.props.credentials,
          'application/json',
          JSON.stringify(obj),
          (resp) => {
            this.setState(old => ({create: false}))
          },
          (err) => {
            this.setState({error: err})
          }
        )
      })
    } else {
      return (
        <div>
          <HttpGet
            url={this.props.url + this.props.partial}
            credentials={this.props.credentials}
            render={(result) => (
              <div>
                <HttpGetSwitch result={result}
                  onJson={json => {
                    let btn
                    const collection = json.collection
                    if (collection.template !== undefined) {
                      btn = <button onClick={() => this.setState(old => ({
                        create: true,
                        template: collection.template
                      }))}>Create</button>
                    }
                    return (
                      <div>
                        {btn}
                        <Paginator response={json} onChange={nUrl => result.setUrl(this.props.url + nUrl)} />
                        <ul>
                          {collection.items.map(item =>
                            <li key={item.data.find(d => d.name === 'checklist_id').value}>
                              <button onClick={() => this.props.onSelectChecklist(item.data.find(d => d.name === 'checklist_id').value)}> {item.data.find(d => d.name === 'name').value}</button> - {item.data.find(d => d.name === 'completion_date').value}
                            </li>)}
                        </ul>
                      </div>
                    )
                  }}
                />
              </div>
            )} />
        </div>
      )
    }
  }

  presentError (error) {
    return <div>
      <h2>An error occurred</h2>
      <h3>{error.message}</h3>
      <button type='submit' onClick={() => {
        console.log('Button Click')
        this.setState({error: undefined})
      }}>Retry</button>
    </div>
  }
}
