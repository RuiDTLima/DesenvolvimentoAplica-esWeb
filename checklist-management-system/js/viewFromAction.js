/**
 * Generates a view based on a siren action
 */
export default (action, actionGenerator, getElementValue, request, onReturn) => {
  return actionGenerator(
    action,
    () => {
      let params = { }
      action.fields.forEach(f => {
        params[f.name] = getElementValue(f.name)
      })
      request(params)
    },
    onReturn
  )
}
