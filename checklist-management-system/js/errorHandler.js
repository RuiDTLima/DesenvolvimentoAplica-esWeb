import React from 'react'
import presentError from './presentError'

export default (error, message, cb) => {
  if (!error.message.includes('404')) {
    return presentError({message: error.message.substring(4)}, cb)
  }
  return <div><h3>{message}</h3></div>
}
