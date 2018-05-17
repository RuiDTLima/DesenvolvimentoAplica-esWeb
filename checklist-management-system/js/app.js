import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import Checklists from './checklists'
import Checklist from './checklist'
import ChecklistItem from './checklistitem'
import ChecklistTemplates from './checklisttemplates'
import Template from './checklistTemplate'
import TemplateItem from './templateItem'
import Login from './login'
import Nav from './nav'

const url = 'http://localhost:8080'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      'credentials': '',
      'home': { }
    }
  }

  menu (username, password) {
    fetch(`${url}/`)
      .then(resp => {
        if (resp.status === 200) {
          return resp.json().then(h => {
            this.setState(old => ({
              'credentials': Buffer.from(`${username}:${password}`).toString('base64'),
              'home': h
            }))
          })
        }
      })
  }

  formGenerator (template, onCreate) {
    return (
      <form onSubmit={(ev) => onCreate(ev)}>
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

  actionGenerator (action, onClick, onReturn) {
    if (!action.fields) action.fields = []
    return (
      <div>
        <h1>{action.title}</h1>
        {action.fields.map(f =>
          <div key={f.name}>
            {
              f.type !== 'hidden' &&
              <label>{f.name}</label>
            }
            <input type={f.type} name={f.name} value={f.value} id={f.name} required />
          </div>
        )}
        <button onClick={() => onClick()}>Save</button>
        <button onClick={() => onReturn()}>Back</button>
      </div>
    )
  }

  actionsForm (path, method, fields, onSuccess) {
    return (
      <form onSubmit={(ev) => onSuccess(ev)}>
        {fields.map(field =>
          <div key={field.name}>
            {field.type !== 'hidden' ? <label type={field.type}>{field.title}</label> : ''}
            <input type={field.type} name={field.name} value={field.value} required />
          </div>
        )}
        <button>Save</button>
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
                  onSuccess={(username, password) => { this.menu(username, password) }}
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
                <Checklists
                  url={url}
                  partial={this.state.home.resources['/checklists'].href}
                  credentials={this.state.credentials}
                  formGenerator={this.formGenerator}
                  onSelectChecklist={(id) => history.push(`/checklists/${id}`)}
                />
              )
            }} />
            <Route exact path='/checklists/:id' render={({match, history}) => {
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              const template = this.state.home.resources['/checklist']
              return (
                <Checklist
                  url={url}
                  partial={template['href-template'].replace(/{checklist_id}/i, `${match.params.id}`)}
                  credentials={this.state.credentials}
                  actionGenerator={this.actionGenerator}
                  onSelectTemplate={(templateId) => history.push(`/checklisttemplates/${templateId}`)}
                  onSelectItem={(itemId, url) => history.push({
                    pathname: `/checklists/${match.params.id}/checklistitems/${itemId}`,
                    state: { itemPath: url }
                  })}
                  onDelete={() => history.push('/checklists')}
                />)
            }} />
            <Route exact path='/checklists/:listId/checklistitems/:itemId' render={({match, history}) => {
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              return (
                <ChecklistItem
                  url={url}
                  partial={history.location.state.itemPath.replace(/:listId/i, `${match.params.listId}`).replace(/:itemId/i, `${match.params.itemId}`)}
                  credentials={this.state.credentials}
                  actionGenerator={this.actionGenerator}
                  onDelete={() => history.push(`/checklists/${match.params.listId}`)}
                />)
            }} />
            <Route exact path='/checklisttemplates' render={({match, history}) => {
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              return <ChecklistTemplates
                url={url}
                partial={this.state.home.resources['/checklisttemplates'].href}
                credentials={this.state.credentials}
                history={history}
                formGenerator={this.formGenerator}
              />
            }} />
            <Route exact path='/checklisttemplates/:checklisttemplate_id' render={({match, history}) => {
              // ERROR
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              return <Template
                baseUrl={url}
                specificUrl={'/checklisttemplates/' + match.params.checklisttemplate_id}
                credentials={this.state.credentials}
                history={history}
                actionsForm={this.actionsForm}
              />
            }} />
            <Route exact path='/checklisttemplates/:checklisttemplate_id/templateitems/:templateitem_id' render={({match, history}) => {
              if (this.state.credentials === '') {
                return <Redirect to='/' />
              }
              return <TemplateItem
                baseUrl={url}
                specificUrl={`/checklisttemplates/${match.params.checklisttemplate_id}/templateitems/${match.params.templateitem_id}`}
                credentials={this.state.credentials}
                history={history}
                actionsForm={this.actionsForm}
              />
            }}
            />
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
