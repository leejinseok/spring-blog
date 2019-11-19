<template>
  <button class="btn btn-danger btn-sm btn-logout" type="button" @click="logout">
    로그아웃
  </button>
</template>

<script>
export default {
  methods: {
    logout: async function() {
      try {
        const result = await this.$axios({
          url: '/api/v1/auth/logout',
          method: 'post'
        });

        console.log(result);
        this.$store.commit('user/reset');
        this.$router.push('/admin');
      } catch (e) {
        console.error(e);
        console.error(e.message);
      }
    }
  }
}
</script>

<style scoped>
.btn-logout {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 12px;
}
</style>