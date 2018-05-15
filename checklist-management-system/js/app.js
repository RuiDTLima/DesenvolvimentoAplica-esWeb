import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import List from './checklists'
import Login from './login'
import Nav from './nav'

const url = 'http://localhost:8080'
let home

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {'credentials': ''}
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

  formGenerator (template, onCreate) {
    return (
      <form action={url} method='POST' onSubmit={(ev) => onCreate(ev)}>
        {template.data.map(t =>
          <div key={t.name}>
            <label>{t.prompt}</label>
            <input type='text' name={t.name} required />
          </div>
        )}
        <button>Create</button>
      </form>
    )
  }

  render () {
    return (
      <BrowserRouter>
        <div>
          <Nav credentials={this.state.credentials} onLogout={() => this.setState(old => ({'credentials': ''}))} />
          <Switch>
            <Route exact path='/' render={() => {
              if (this.state.credentials !== '') {
                return <Redirect to='/menu' />
              }
              return (
                <Login
                  url={url}
                  onSuccess={(username, password) => { this.setState(old => ({'credentials': btoa(`${username}:${password}`)})) }}
                  onError={() => new Error('Login failed')}
                />
              )
            }} />
            {/* <Route path='/' render={() => {
              if (this.state.credentials === '') {
                return (
                  <Login
                    url={url}
                    onSuccess={(username, password) => { this.setState(old => ({'credentials': btoa(`${username}:${password}`)})) }}
                    onError={() => new Error('Login failed')}
                  />
                )
              }
            }} /> */}
            <Route exact path='/menu' render={({match, history}) => {
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              this.menu()
              return (
                <div>
                  <div><button onClick={() => history.push('/checklists')}>checklists</button></div>
                  <div><button onClick={() => history.push('/checklisttemplates')}>checklist templates</button></div>
                </div>
              )
            }}
            />
            <Route exact path='/checklists' render={({match, history}) => {
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              return (
                <List
                  url={url + home.resources['/checklists'].href}
                  credentials={this.state.credentials}
                  formGenerator={this.formGenerator}
                />
              )
            }} />
            <Route exact path='/checklisttemplates' render={({match, history}) => {
              // ERROR
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              return <List
                url={url + home.resources['/checklisttemplates'].href}
                credentials={this.state.credentials}
              />
            }} />
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
