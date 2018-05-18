import fetch from 'isomorphic-fetch'

export function request (url, method, credentials, contentType, body, onSuccess, onError) {
  fetch(url, {
    method: method,
    headers: {
      'Authorization': credentials,
      'content-type': contentType
    },
    body: body
  })
    .then(resp => {
      if (resp.status === 204) {
        onSuccess(resp)
      } else if (resp.status >= 400 && resp.status < 500) {
        return onError(new Error(resp.message))
      } else return onError(new Error('server error'))
    })
}
