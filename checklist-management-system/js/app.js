import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import Checklists from './components/checklists'
import Checklist from './components/checklist'
import ChecklistItem from './components/checklistitem'
import ChecklistTemplates from './components/checklisttemplates'
import Template from './components/checklistTemplate'
import TemplateItem from './components/templateItem'
import Login, {Redirect as LoginRedirect} from './login'
import Nav from './nav'
import PrivateRoute from './privateRoute'
import presentError from './presentError'

const url = 'http://35.234.132.39/api'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      'credentials': '',
      'home': { }
    }
  }

  /**
   * Requests to the API the home resource.
   * @param {string} user
   */
  menu (user) {
    fetch(`${url}/`)
      .then(resp => {
        if (resp.status === 200) {
          return resp.json().then(h => {
            this.setState(old => ({
              'credentials': user,
              'home': h
            }))
          })
        } else {
          resp.json().then((problemJson) => this.setState({error: new Error(problemJson.detail)}))
        }
      })
      .catch(err => {
        this.setState({error: err})
      })
  }

  /**
   * Shows the form containing all the inputs needed for the operation.
   * This is used for actions described in collection+json.
   * @param {object} template
   * @param {function} onCreate
   * @param {function} onReturn
   */
  formGenerator (template, onCreate, onReturn) {
    return (
      <div>
        <form onSubmit={(ev) => onCreate(ev)}>
          {template.data.map(t =>
            <div key={t.name}>
              <label>{t.prompt}</label>
              <input type='text' name={t.name} required />
            </div>
          )}
          <button>Create</button>
        </form>
        <button onClick={() => onReturn()}>Back</button>
      </div>
    )
  }

  /**
   * Shows the form containing all the inputs needed for the operation.
   * This is used for actions described in siren+json.
   * @param {object} action
   * @param {function} onClick
   * @param {function} onReturn
   */
  actionGenerator (action, onClick, onReturn) {
    return (
      <div>
        <h3>{action.title}</h3>
        {action.fields.map(field =>
          <div key={field.name}>
            {
              field.type !== 'hidden' &&
              <label>{field.name}</label>
            }
            <input type={field.type} name={field.name} value={field.value} id={field.name} required />
          </div>
        )}
        <button onClick={() => onClick()}>Save</button>
        <button onClick={() => onReturn()}>Back</button>
      </div>
    )
  }

  render () {
    if (this.state.error) {
      return presentError(this.state.error, () => this.setState({error: undefined}))
    }
    return (
      <BrowserRouter>
        <div>
          <Nav credentials={this.state.credentials} onLogout={(history) => {
            history.replace('/')
            this.setState({credentials: '', home: undefined})
          }} />
          <Switch>
            <Route exact path='/' render={({history}) => {
              if (this.state.credentials !== '') {
                if (history.location.state) {
                  return <Redirect to={history.location.state.return} />
                }
                return <Redirect to='/menu' />
              }
              return <Login onSuccess={(user) => this.menu(user)} />
            }} />
            <Route exact path='/redirect2' component={LoginRedirect} />
            <PrivateRoute credentials={this.state.credentials} exact path='/menu' render={({match, history}) => {
              return (
                <div>
                  <div><button onClick={() => history.push('/checklists')}>checklists</button></div>
                  <div><button onClick={() => history.push('/checklisttemplates')}>checklist templates</button></div>
                </div>
              )
            }}
            />
            <PrivateRoute credentials={this.state.credentials} exact path='/checklists' render={({match, history}) => {
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
            <PrivateRoute credentials={this.state.credentials} exact path='/checklists/:id' render={({match, history}) => {
              const template = this.state.home.resources['/checklist']
              return (
                <Checklist
                  url={url}
                  partial={template['href-template'].replace(/{checklist_id}/i, `${match.params.id}`)}
                  credentials={this.state.credentials}
                  actionGenerator={this.actionGenerator}
                  onSelectTemplate={(templateId) => history.push(`/checklisttemplates/${templateId}`)}
                  onSelectItem={(itemId) => history.push(`/checklists/${match.params.id}/checklistitems/${itemId}`)}
                  onReturn={() => history.push('/checklists')}
                />)
            }} />
            <PrivateRoute credentials={this.state.credentials} exact path='/checklists/:listId/checklistitems/:itemId' render={({match, history}) => {
              const template = this.state.home.resources['/checklist']
              return (
                <ChecklistItem
                  url={url}
                  partial={template['href-template'].replace(/{checklist_id}/i, `${match.params.listId}`)}
                  itemId={match.params.itemId}
                  credentials={this.state.credentials}
                  actionGenerator={this.actionGenerator}
                  onReturn={() => history.push(`/checklists/${match.params.listId}`)}
                />)
            }} />
            <PrivateRoute credentials={this.state.credentials} exact path='/checklisttemplates' render={({match, history}) => {
              return <ChecklistTemplates
                url={url}
                partial={this.state.home.resources['/checklisttemplates'].href}
                credentials={this.state.credentials}
                onSelect={(id) => history.push(`/checklisttemplates/${id}`)}
                formGenerator={this.formGenerator}
              />
            }} />
            <PrivateRoute credentials={this.state.credentials} exact path='/checklisttemplates/:checklisttemplate_id' render={({match, history}) => {
              const template = this.state.home.resources['/checklisttemplate']
              return <Template
                baseUrl={url}
                specificUrl={template['href-template'].replace(/{checklisttemplate_id}/i, `${match.params.checklisttemplate_id}`)}
                credentials={this.state.credentials}
                onClick={(url) => history.push(url)}
                onReturn={() => history.push('/checklisttemplates')}
                actionGenerator={this.actionGenerator}
              />
            }} />
            <PrivateRoute credentials={this.state.credentials} exact path='/checklisttemplates/:checklisttemplate_id/templateitems/:templateitem_id' render={({match, history}) => {
              const template = this.state.home.resources['/checklisttemplate']
              return <TemplateItem
                baseUrl={url}
                specificUrl={template['href-template'].replace(/{checklisttemplate_id}/i, `${match.params.checklisttemplate_id}`)}
                itemId={match.params.templateitem_id}
                credentials={this.state.credentials}
                onClick={(url) => history.push(url)}
                onReturn={() => history.push(`/checklisttemplates/${match.params.checklisttemplate_id}`)}
                actionGenerator={this.actionGenerator}
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
