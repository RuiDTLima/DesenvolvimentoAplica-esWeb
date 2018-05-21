import React from 'react'
import {Route, Redirect} from 'react-router-dom'

/**
 * Used for routes that require authorization.
 * Validates if credentials exist to allow the presentation
 * of the view.
 */
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
