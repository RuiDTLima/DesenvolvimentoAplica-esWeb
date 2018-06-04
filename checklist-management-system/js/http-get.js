import React from 'react'
import fetch from 'isomorphic-fetch'
import {makeCancellable} from './promises'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.setUrl = this.setUrl.bind(this)
    this.setQuery = this.setQuery.bind(this)
    this.state = {
      loading: true,
      url: props.url,
      credentials: props.credentials
    }
  }

  render () {
    const result = {...this.state, setUrl: this.setUrl, setQuery: this.setQuery}
    return this.props.render(result)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    if (nextProps.url === prevState.url) return null
    return {
      loading: true,
      url: nextProps.url,
      responde: undefined,
      error: undefined,
      json: undefined
    }
  }

  componentDidMount () {
    this.load(this.props.url, this.props.credentials)
  }

  componentDidUpdate (oldProps, oldState) {
    if (this.state.loading) this.load(this.state.url, this.state.credentials)
  }

  componentWillUnmount () {
    if (this.promise) {
      this.promise.cancel()
    }
  }

  setQuery (query) {
    console.log(`setQuery(${query})`)
    this.setState({
      url: this.props.url + '?' + query,
      loading: true
    })
  }

  setUrl (url) {
    this.setState({
      url: url,
      loading: true
    })
  }

  load (url) {
    console.log(`load(${url})`)
    if (this.promise) {
      this.promise.cancel()
    }
    this.promise = makeCancellable(fetch(url, {headers: {'Authorization': `Bearer ${this.state.credentials.access_token}`}}))
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error(`${resp.status} Unable to access content`)
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct.startsWith('application/vnd.collection+json') || ct.startsWith('application/vnd.siren+json')) {
          return resp.json().then(json => [resp, json])
        }
        throw new Error(`${resp.status} unexpected content type ${ct}`)
      })
      .then(([resp, json]) => {
        this.setState({
          loading: false,
          json: json,
          response: resp,
          error: undefined
        })
        this.promise = undefined
        return json
      })
      .catch(error => {
        this.setState({
          loading: false,
          error: error,
          json: undefined,
          response: undefined
        })
        this.promise = undefined
      })
  }
}
