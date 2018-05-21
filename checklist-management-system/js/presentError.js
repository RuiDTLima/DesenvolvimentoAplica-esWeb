import React from 'react'

export default (error, cb) => {
  return <div>
    <h2>An error occurred</h2>
    <h3>{error.message}</h3>
    <button type='submit' onClick={() => cb()}>Retry</button>
  </div>
}
