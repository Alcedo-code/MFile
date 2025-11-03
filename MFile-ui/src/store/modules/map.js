const state = {
  earth: null,
  Draw: null
}
const mutations = {
  SET_EARTH: (state, earth) => {
    state.earth = earth
  },
  SET_DRAW: (state, Draw) => {
    state.Draw = Draw
    //  sessionStorage.setItem("Draw", Draw)
  },
  SET_MAP_LAYERS: (state, data) => {
    state.data = data
    //  sessionStorage.setItem("Draw", Draw)
  }
}
export default {
  namespaced: true,
  state,
  mutations
}
