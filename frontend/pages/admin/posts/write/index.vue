<template>
  <div class="container">
    <div class="form-container">
      <form @submit="submit($event)">
        <input v-model="title" type="text" placeholder="타이틀">
        <textarea v-model="content" placeholder="내용"></textarea>
        <button type="submit">등록</button>
      </form>
    </div>
  </div>
</template>

<script>
import qs from 'qs';

export default {
  data() {
    return {
      title: '',
      content: ''
    }
  },
  methods: {
    submit: async function(event) {
      event.preventDefault();
      const data = {
        title: this.$data.title,
        content: this.$data.content
      };

      try {
        const result = await this.$axios({
          url: '/api/v1/posts',
          method: 'post',
          data
        });

        this.$router.push('/admin/posts');
      } catch (e) {
        console.log(e);
        console.log(e.message);
        alert('에러발생');
      }
    }
  }
}
</script>

<style scoped>
.container {
  text-align: center;
}

.form-container {
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
}

.form-container input {
  margin-top: 30px;
}

.form-container input,
.form-container textarea {
  width: 100%;
}

.form-container textarea {
  margin-top: 10px;
}
</style>