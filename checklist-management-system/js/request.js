import fetch from 'isomorphic-fetch'

export function request (url, method, credentials, contentType, body, onSuccess, onError) {
  fetch(url, {
    method: method,
    headers: {
      'Authorization': `Basic ${credentials}`,
      'content-type': contentType
    },
    body: body
  })
    .then(resp => {
      if (resp.status === 204) {
        onSuccess(resp)
      } else if (resp.status === 404) {
        throw new Error('Resource does not exist.')
      } else if (resp.status >= 400 && resp.status < 500) {
        return resp.json().then((problemJson) => {
          throw new Error(problemJson.detail)
        })
      } else throw new Error('Server error')
    })
    .catch(err => {
      return onError(err)
    })
}
