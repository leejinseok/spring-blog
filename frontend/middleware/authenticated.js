export default async function ({ $axios, store, redirect }) {

  if (!store.state.user.init) {
    const { data } = await $axios({
      url: '/api/v1/auth/session',
      method: 'get'
    });

    if (data) {
      store.commit('user/set', data);
    } else {
      return redirect('/admin');
    }
  }
}