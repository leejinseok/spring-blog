export default async function ({ $axios, store, redirect }) {

  if (!store.state.user.init) {
    try {
      const { data } = await $axios({
        url: '/api/v1/auth/session',
        method: 'get'
      });
  
      if (data) {
        store.commit('user/set', data);
      } else {
        return redirect('/admin');
      }
    } catch (e) {
      console.error(e.message);
    }
  }
}