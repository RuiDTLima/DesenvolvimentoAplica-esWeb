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
      create: false
    }
    this.formView = this.formView.bind(this)
    this.display = this.display.bind(this)
  }

  componentWillUnmount () {
    if (this.promise) {
      this.promise.cancel()
    }
  }

  render () {
    if (this.state.error) {
      return presentError(this.state.error, () => this.setState({error: undefined}))
    }
    if (this.state.create) {
      return this.formView()
    }
    return this.display()
  }

  /**
   * Creates the requests to be executed for the action of the form.
   */
  formView () {
    return this.props.formGenerator(this.state.template, (ev) => {
      ev.preventDefault()
      const obj = { }
      this.state.template.data.forEach(d => { obj[d.name] = (document.getElementsByName(d.name)[0].value) })
      this.promise = request(
        this.props.url + this.props.partial,
        'POST',
        this.props.credentials,
        'application/json',
        JSON.stringify(obj),
        (resp) => {
          this.promise = undefined
          this.setState(old => ({create: false}))
        },
        (err) => {
          this.promise = undefined
          this.setState({error: err})
        }
      )
    }, () => this.setState({create: false}))
  }

  /**
   * Central view of every information regarding the list of checklists.
   */
  display () {
    return (
      <div>
        <HttpGet
          url={this.props.url + this.props.partial}
          credentials={this.props.credentials}
          render={(result) => (
            <div>
              <HttpGetSwitch result={result}
                onError={err => {
                  return errorHandler(err, 'Checklists not found.', () => this.setState({error: undefined}))
                }}
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
