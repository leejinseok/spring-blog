export const state = () => ({
  data: null,
  init: false
})

export const mutations = {
  set (state, data) {
    state.data = data;
    state.init = true;
  }
}