import React from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'

export default ({url, credentials}) => (
  <div>
    <HttpGet
      url={url}
      credentials={credentials}
      render={(result) => (
        <div>
          <HttpGetSwitch result={result}
            onJson={json => (
                <ul>
                    {json.collection.items.map(item => <li key={item.data[1].value}>{item.data[0].value}</li>)}
                </ul>
            )}
          />
        </div>
      )} />
  </div>
)