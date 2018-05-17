import React from 'react'

export default ({response, onChange}) => {
  console.log(response)
  const linkHeader = response && response.collection.links
  console.log(linkHeader)
  const hasNext = linkHeader.find(l => l.rel === 'next')
  const hasPrev = linkHeader.find(l => l.rel === 'previous')
  return (
    <div>
      <button disabled={!hasPrev}
        onClick={() => onChange(hasPrev.href)} >previous</button>
      <button disabled={!hasNext}
        onClick={() => onChange(hasNext.href)} >next</button>
    </div>
  )
}
