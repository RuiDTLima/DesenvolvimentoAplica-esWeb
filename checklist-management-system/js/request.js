import fetch from 'isomorphic-fetch'
import {makeCancellable} from './promises'

/**
 * Used to make state-changing HTTP requests, e.g. POST, PUT and DELETE.
 * The result of this operation is not presentable.
 * @param {string} url
 * @param {string} method
 * @param {object} credentials
 * @param {string} contentType
 * @param {object} body
 * @param {function} onSuccess
 * @param {function} onError
 */
export function request (url, method, credentials, contentType, body, onSuccess, onError) {
  return makeCancellable(fetch(url, {
    method: method,
    headers: {
      'Authorization': `Basic ${credentials}`,
      'content-type': contentType
    },
    body: body
  }))
    .then(resp => {
      if (resp.status === 204) {
        return onSuccess(resp)
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
