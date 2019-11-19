export default async function ({ $axios, store, redirect }) {

  if (!store.state.user.init) {
    try {
      const { data } = await $axios({
        url: '/api/v1/auth/session',
        method: 'get'
      });
  
      store.commit('user/set', data);
    } catch (e) {
      redirect('/admin');
      console.error('authenticated', e.message);
    }
  }
}