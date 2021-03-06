import React from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import Paginator from '../paginator'
import errorHandler from '../errorHandler'

export default ({url, partial, credentials, onSelectItem}) => (
  <div>
    <HttpGet
      url={url + partial}
      credentials={credentials}
      render={(result) => (
        <div>
          <HttpGetSwitch result={result}
            onError={err => {
              return errorHandler(err, 'Checklist Items not found.', () => this.setState({error: undefined}))
            }}
            onJson={json => {
              return (
                <div>
                  <Paginator response={json} onChange={nUrl => result.setUrl(url + nUrl)} />
                  <ul>
                    {json.collection.items.length !== 0 &&
                      <h3>Checklist Items</h3>
                    }
                    {json.collection.items.map(item =>
                      <li key={item.data.find(d => d.name === 'checklistitem_id').value}>
                        <button onClick={() => onSelectItem(item.data.find(d => d.name === 'checklistitem_id').value)}> {item.data.find(d => d.name === 'name').value}</button>
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
