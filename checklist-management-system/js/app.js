import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import fetch from 'isomorphic-fetch'
const issuesTempl = new URITemplate('/issues/{url}')
// const fetch = require('isomorphic-fetch')

// const home = new URITemplate(`/`).expand({url: 'http://localhost:8080/'})
const url = 'http://localhost:8080'
let historyf

export default class extends React.Component {
  constructor(props) {
    super(props)
    this.state = {'credentials': ''}
    this.saveCredentials = this.saveCredentials.bind(this)
    this.login = this.login.bind(this)
    this.getChecklists = this.getChecklists.bind(this)
    this.getChecklistTemplates = this.getChecklistTemplates.bind(this)
  }

  login() {
    return (
    <div>
        <form onSubmit={this.saveCredentials}>
            <div>
                <label>Username: </label>
                <input type='text' name='username' required />
            </div>
            <div>
                <label>Password: </label>
                <input type='password' name='password' required />                        
            </div>
            <div>
                <button>Submit</button>
                {/* <input type='submit' value='Submit' onSubmit={this.login} />    */}
            </div>
        </form>             
    </div>
    )
  }

  logout() {
    this.setState(old => ({'credentials': ''}))
    historyf.push('/') // ===========================
  }

  saveCredentials(ev) {
    const username = document.getElementsByName('username')[0].value
    const password = document.getElementsByName('password')[0].value
    ev.preventDefault()    
    fetch(`${url}/users/${username}`)
    .then(resp => {
      if(resp.status == 200) {
        return resp.json().then(user => {
          console.log(user)
          if(user.properties.password == password) {
            this.setState(old => ({'credentials': btoa(`${username}:${password}`)}))
            historyf.push('/menu') 
          } else {
            new Error('Error')
          }
        })
      }
    })
  }

  menu(){
    let h = ''
    fetch(`${url}/`)
    .then(resp => {
      if(resp.status == 200) {
        return resp.json().then(home => {
          console.log(home)
          h = home
        })
      }
    })
    let authenticated;
    if(this.state.credentials == '') {
      authenticated = <div><button onClick={this.login}>log in</button></div>
    } else authenticated = <div><button onClick={this.logout}>log out</button></div>
          
    return (
      <div>
        <div><button onClick={() => this.getChecklists(h)}>checklists</button></div>
        <div><button onClick={() => this.getChecklistTemplates(h)}>checklist templates</button></div>
        {authenticated}
      </div>
    )
  }

  getChecklists(home) {
    let path = url + home.resources['/checklists'].href
    console.log(path)
    fetch(path, {method: 'GET', headers: {'Authorization': this.state.credentials}})
    .then(resp => {
      if(resp.status === 200) {
        return resp.json().then(checklists => {
          console.log(checklists)
        })
      }
    })
  }

  getChecklistTemplates(home) {
    let path = url + home.resources['/checklisttemplates'].href
    console.log(path)
    fetch(path, {headers: {'Authorization': this.state.credentials}})
    .then(resp => {
      if(resp.status === 200) {
        return resp.json().then(checklisttemplates => {
          console.log(checklisttemplates)
        })
      }
    })
  }

  render () {
    return (
      <BrowserRouter>
        <div>
          <Switch>
            <Route exact path='/' render={({history}) => {
              historyf = history
              return this.login()
            }
              } />
            <Route exact path='/menu' render={() => this.menu()} />
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
