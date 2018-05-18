import React, {Component} from 'react'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      hasError: false
    }
  }

  componentDidCatch (error, info) {
    console.log('error ' + error)
    console.log('info ' + info)
    this.setState(old => ({hasError: true}))
  }

  render () {
    console.log(this.state.hasError)
    if (this.state.hasError) {
      return <div>
        <h1>Error</h1>
        {this.props.children}
      </div>
    }
    return this.props.children
  }
}
