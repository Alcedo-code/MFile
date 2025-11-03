const state = {
  parameter: JSON.parse(sessionStorage.getItem('parameter')) || {},
  mapping: JSON.parse(sessionStorage.getItem('mapping')) || {},
  details: JSON.parse(sessionStorage.getItem('details')) || {}
}
const mutations = {
  SET_PARAMETER: (state, parameter) => {
    state.parameter = parameter
    sessionStorage.setItem('parameter', JSON.stringify(parameter))
  },
  SET_MAPPING: (state, mapping) => {
    state.mapping = mapping
    sessionStorage.setItem('mapping', JSON.stringify(mapping))
  },
  SET_DETAILS: (state, details) => {
    state.details = details
    sessionStorage.setItem('details', JSON.stringify(details))
  }
}
export default {
  namespaced: true,
  state,
  mutations
}
