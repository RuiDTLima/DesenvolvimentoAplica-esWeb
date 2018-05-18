import React from 'react'
import {Route, Redirect} from 'react-router-dom'

export default ({ render, credentials, ...rest }) => (
  <Route {...rest} render={(props) => {
    return credentials === ''
      ? <Redirect to={{
        pathname: '/',
        state: {'return': props.history.location.pathname}
      }} />
      : render(props)
  }} />
)
