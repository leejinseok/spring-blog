<template>
  <div class="container">
    <h2>관리자페이지</h2>

    <div class="form-container">
      <form @submit="submit($event)">
        <input type="email" v-model="email" placeholder="이메일">
        <input type="password" v-model="password" placeholder="패스워드">
        <button type="submit">로그인</button>
      </form>
    </div>
  </div>
</template>

<script>
import qs from 'qs';

export default {
  layout: 'admin',
  async asyncData(context) {
    const { redirect, store } = context;
    if (store.state.user.init) {
      redirect('/admin/posts');
    }
  },
  data() {
    return {
      email: 'sonaky47@naver.com',
      password: '1111'
    }
  },
  methods: {
    submit: async function(event) {
      event.preventDefault();
      const { $axios, $store, $route, $router } = this;
      const { email, password } = this;
      const data = {
        email,
        password
      };
      try {
        const result = await $axios({
          url: '/api/v1/auth/login',
          method: 'post',
          headers: { 'content-type': 'application/x-www-form-urlencoded' },
          data: qs.stringify(data)
        });

        $store.commit('user/set', result.data.user);
        $router.push('/admin/posts');
      } catch (err) {
        if (err.response.data.message.includes('Email or Password not valid')) {
          alert('이메일 혹은 패스워드를 확인해주세요.');
        } else {
          alert('로그인 실패');
        }

        console.error(err);
      }
    }
  }
}
</script>

<style>
h2 {
  font-size: 24px;
  margin-top: 50px;
  text-align: center;
}

.form-container {
  text-align: center;
}

.form-container form {
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.form-container form input {
  display: block;
  margin-left: auto;
  margin-right: auto;
  margin-top: 6px;
}

.form-container form button {
  margin-top: 10px;
  border: 1px solid #eaeaea;
}
</style>