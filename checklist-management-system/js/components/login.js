import React from 'react'
import fetch from 'isomorphic-fetch'
import {request} from '../request'

export default ({url, onSuccess, onError}) => (
  <div>
    <form onSubmit={(ev) => saveCredentials(ev, url, onSuccess, onError)}>
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
      </div>
    </form>
  </div>
)

function saveCredentials (ev, url, onSuccess, onError) {
  const username = document.getElementsByName('username')[0]
  const password = document.getElementsByName('password')[0]
  ev.preventDefault()
  fetch(`${url}/users/${username.value}`)
    .then(resp => {
      if (resp.status === 200) {
        return resp.json().then(user => {
          if (user.properties.password === password.value) {
            onSuccess(username.value, password.value)
          } else {
            throw new Error('The user info is not correct.')
          }
        })
      } else if (resp.status === 404) {
        throw new Error('The user does not exist.')
      } else if (resp.status >= 500) {
        throw new Error('Server error.')
      } else if (resp.status >= 400) {
        return resp.json().then((problemJson) => {
          throw new Error(problemJson.detail)
        })
      }
    })
    .catch(err => {
      return onError(err)
    })
}
