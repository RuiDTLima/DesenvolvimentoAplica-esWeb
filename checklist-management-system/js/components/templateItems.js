import React from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import Paginator from '../paginator'
import errorHandler from '../errorHandler'

export default ({baseUrl, partialUrl, credentials, templateId, onClick}) => (
  <HttpGet
    url={baseUrl + partialUrl}
    credentials={credentials}
    render={(result => (
      <HttpGetSwitch result={result}
        onError={err => {
          return errorHandler(err, 'Checklist Template Items not found.', () => this.setState({error: undefined}))
        }}
        onJson={items => {
          return (
            <div>
              {items.collection.items.length !== 0 ? (<h3>Template Items</h3>) : ''}
              <Paginator response={items} onChange={nUrl => result.setUrl(baseUrl + nUrl)} />
              <ul>
                {items.collection.items.map(item =>
                  <li key={item.data.find(d => d.name === 'templateitem_id').value}>
                    <button onClick={() => {
                      onClick(`/checklisttemplates/${templateId}/templateitems/${item.data.find(d => d.name === 'templateitem_id').value}`)
                    }}>
                      {item.data.find(d => d.name === 'name').value}
                    </button>
                  </li>
                )}
              </ul>
            </div>
          )
        }}
      />
    ))
    }
  />
)
