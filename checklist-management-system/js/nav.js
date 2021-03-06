import React from 'react'
import {withRouter} from 'react-router-dom'

/**
 * Creates the nav present in all views.
 */
export default withRouter(({credentials, onLogout, history}) => {
  if (credentials === '') {
    return <nav />
  }
  return (
    <nav>
      <button onClick={() => history.push('/menu')}>Menu</button>
      <button onClick={() => onLogout(history)}>Log Out</button>
    </nav>
  )
})
