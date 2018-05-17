import React from 'react'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'

export default ({url, partial, credentials, onSelectItem}) => (
  <div>
    <HttpGet
      url={url + partial}
      credentials={credentials}
      render={(result) => (
        <div>
          <HttpGetSwitch result={result}
            onJson={json => {
              return (
                <div>
                  <ul>
                    {json.collection.items.length !== 0 &&
                      <h3>Checklist Items</h3>
                    }
                    {json.collection.items.map(item =>
                      <li key={item.data.find(d => d.name === 'checklistitem_id').value}>
                        <button onClick={() => onSelectItem(item.data.find(d => d.name === 'checklistitem_id').value, item.href)}> {item.data.find(d => d.name === 'name').value}</button>
                      </li>)}
                  </ul>
                </div>
              )
            }}
          />
        </div>
      )} />
  </div>
)
