import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import fetch from 'isomorphic-fetch'
import List from './checklists'
import Login from './login'
const issuesTempl = new URITemplate('/issues/{url}')
// const fetch = require('isomorphic-fetch')

// const home = new URITemplate(`/`).expand({url: 'http://localhost:8080/'})
const url = 'http://localhost:8080'
let home

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {'credentials': ''}
    this.getChecklistTemplates = this.getChecklistTemplates.bind(this)
    this.onLoginSuccess = this.onLoginSuccess.bind(this)
    this.onLoginError = this.onLoginError.bind(this)
  }

  logout () {
    this.setState(old => ({'credentials': ''}))
  }

  menu () {
    fetch(`${url}/`)
      .then(resp => {
        if (resp.status === 200) {
          return resp.json().then(h => {
            console.log(h)
            home = h
          })
        }
      })
    let authenticated
    if (this.state.credentials === '') {
      authenticated = <div><button onClick={this.login}>log in</button></div>
    } else authenticated = <div><button onClick={this.logout}>log out</button></div>
  }

  getChecklistTemplates (home) {
    let path = url + home.resources['/checklisttemplates'].href
    console.log(path)
    fetch(path, {headers: {'Authorization': this.state.credentials}})
      .then(resp => {
        if (resp.status === 200) {
          return resp.json().then(checklisttemplates => {
            console.log(checklisttemplates)
          })
        }
      })
  }

  onLoginSuccess (username, password, history) {
    this.setState(old => ({'credentials': btoa(`${username}:${password}`)}))
    history.push('/menu')
  }

  onLoginError () {
    return new Error('Login failed')
  }

  render () {
    return (
      <BrowserRouter>
        <div>
          <Switch>
            <Route exact path='/' render={({history}) => (
              <Login
                url={url}
                onSuccess={(username, password) => { this.onLoginSuccess(username, password, history) }}
                onError={this.onLoginError}
              />
            )} />
            <Route exact path='/menu' render={({match, history}) => {
              this.menu()
              return (
                <div>
                  <div><button onClick={() => history.push('/checklists')}>checklists</button></div>
                  <div><button onClick={() => history.push('/checklisttemplates')}>checklist templates</button></div>
                </div>
              )
            }}
            />
            <Route exact path='/checklists' render={({match, history}) => (
              <List
                url={url + home.resources['/checklists'].href}
                credentials={this.state.credentials}
              />
            )} />
            <Route exact path='/checklisttemplates' render={({match, history}) => (
              <List
                url={url + home.resources['/checklisttemplates'].href}
                credentials={this.state.credentials}
              />
            )} />
            <Route path='/' render={({history}) =>
              <div>
                <h2>Route not found</h2>
                <button onClick={() => history.push('/')}>home</button>
              </div>
            } />
          </Switch>
        </div>
      </BrowserRouter>
    )
  }
}
