const state = {
  menu: null,
  children: null,
  data: JSON.parse(sessionStorage.getItem('data')) || {}
}
const mutations = {
  SET_MENU: (state, menu) => {
    state.menu = menu
  },
  SETCHILDREN: (state, children) => {
    state.children = children
  },
  SETDATA: (state, data) => {
    console.log(123333, data)
    state.data = data
    sessionStorage.setItem('data', JSON.stringify(data))
  }
}

export default {
  namespaced: true,
  state,
  mutations
}
