import React from 'react'
import { UserManager } from 'oidc-client'

var mitreIDsettings = {
  authority: 'http://localhost:8080/openid-connect-server-webapp',
  client_id: 'client',
  redirect_uri: 'http://localhost:9000/redirect.html',
  popup_redirect_uri: 'http://localhost:9000/redirect.html',
  post_logout_redirect_uri: 'http://localhost:9000/user-manager-sample.html',
  response_type: 'token id_token',
  scope: 'openid email profile',
  silent_redirect_uri: 'http://localhost:9000/user-manager-sample-silent.html',
  automaticSilentRenew: true,
  filterProtocolClaims: true,
  loadUserInfo: true
}

const mgr = new UserManager(mitreIDsettings)

export default ({onSuccess}) => (
  <button onClick={() => login(onSuccess)}>Login</button>
)

function login (onSuccess) {
  mgr.getUser()
    .then(user => {
      mgr.signinPopup()
        .then(user => {
          onSuccess(user)
        })
    })
}
