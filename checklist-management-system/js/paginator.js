import React from 'react'

export default ({response, onChange}) => {
  const linkHeader = response && response.collection.links
  const hasNext = linkHeader.find(l => l.rel === 'next')
  const hasPrev = linkHeader.find(l => l.rel === 'previous')
  return (
    <div>
      {
        hasPrev &&
        <button onClick={() => onChange(hasPrev.href)} >previous</button>
      }
      {
        hasNext &&
        <button onClick={() => onChange(hasNext.href)} >next</button>
      }
    </div>
  )
}
