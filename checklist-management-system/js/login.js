import React from 'react'
import fetch from 'isomorphic-fetch'

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
        {/* <input type='submit' value='Submit' onSubmit={this.login} />    */}
      </div>
    </form>
  </div>
)

function saveCredentials (ev, url, onSuccess, onError) {
  const username = document.getElementsByName('username')[0].value
  const password = document.getElementsByName('password')[0].value
  ev.preventDefault()
  fetch(`${url}/users/${username}`)
    .then(resp => {
      if (resp.status === 200) {
        return resp.json().then(user => {
          console.log(user)
          if (user.properties.password === password) {
            onSuccess(username, password)
          } else {
            onError()
          }
        })
      }
    })
}
