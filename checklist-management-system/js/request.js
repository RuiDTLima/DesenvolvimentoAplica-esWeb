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
        resp.json().then((problemJson) => onError(new Error(problemJson.detail)))
      } else return onError(new Error('server error'))
    })
    .catch(err => {
      return onError(err)
    })
}
