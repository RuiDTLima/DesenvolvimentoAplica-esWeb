import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import fetch from 'isomorphic-fetch'
const issuesTempl = new URITemplate('/issues/{url}')
// const fetch = require('isomorphic-fetch')

const home = new URITemplate(`/`).expand({url: 'http://localhost:8080/'})
let historyf

export default class extends React.Component {
  constructor(props) {
    super(props)
    this.state = {'credentials': ''}
    this.saveCredentials = this.saveCredentials.bind(this)
  }

  login() {
    console.log('chegou')
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

  saveCredentials(ev) {
    const username = document.getElementsByName('username')[0].value
    const password = document.getElementsByName('password')[0].value
    ev.preventDefault()    
    this.setState(old => ({'credentials': btoa(`${username}:${password}`)}))
    historyf.push('/menu') 
  }

  menu(){
    fetch('http://localhost:8080/')
    .then(resp => {
      if(resp.status == 200) {
        return resp.json().then(json => {
          console.log(json)
        })
      }
    })
    return (
      <div>
        <div><button>checklists</button></div>
        <div><button>checklist templates</button></div>
        <div><button>log out</button></div>
      </div>
    )
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
