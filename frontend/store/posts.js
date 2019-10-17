export const state = () => ({
  data: [],
  paginator: null,
  init: false
})

export const mutations = {
  set (state, { data, paginator }) {
    state.data = data;
    state.paginator = paginator;
    state.init = true;
  },
  reset(state) {
    state.data = null;
    state.paginator = null;
    state.init = false;
  }
}