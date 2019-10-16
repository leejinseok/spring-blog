<template>
  <div>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="#">관리자페이지</a></li>
        <li class="breadcrumb-item active"><nuxt-link to="/admin/posts">게시글</nuxt-link></li>
        <li class="breadcrumb-item active" aria-current="page">글쓰기</li>
      </ol>
    </nav>
    <div class="container">
      <div class="form-container">
        <form @submit="submit($event)">
          <div class="form-group">
            <input class="form-control" v-model="title" type="text" placeholder="타이틀">
          </div>
          <div class="form-group">
            <textarea class="form-control" v-model="content" placeholder="내용"></textarea>
          </div>
          <div class="form-group">
            <input class="form-control" type="file" ref="file">
          </div>
          <div class="form-group">
            <button type="submit" class="btn btn-primary">등록</button>     
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs';

export default {
  layout: 'admin',
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
        let result = await this.$axios({
          url: '/api/v1/posts',
          method: 'post',
          data
        });

        const file = this.$refs.file.files[0];
        if (file) {
          const formData = new FormData();
          formData.append('file', file);
          result = await this.$axios({
            url: `/api/v1/posts/${result.data.id}/images`,
            method: 'put',
            data: formData
          });
        }
        
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