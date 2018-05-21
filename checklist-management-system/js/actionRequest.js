import {request} from './request'

/**
   * Executes a state-modifying request to the API.
   * e.g. POST, PUT and DELETE
   * @param {string} url
   * @param {object} action
   * @param {object} credentials
   * @param {object} body
   * @param {function} onSuccess
   * @param {function} onError
   */
export default (url, action, credentials, body, onSuccess, onError) => {
  return request(
    url,
    action.method,
    credentials,
    action.type,
    JSON.stringify(body),
    (resp) => {
      return onSuccess(resp)
    },
    (err) => {
      onError(err)
    }
  )
}
