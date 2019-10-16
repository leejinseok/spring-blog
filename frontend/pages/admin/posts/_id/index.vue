<template>
  <div>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="#">관리자페이지</a></li>
        <li class="breadcrumb-item active"><nuxt-link to="/admin/posts">게시글</nuxt-link></li>
        <li class="breadcrumb-item active" aria-current="page">{{ post.title }}</li>
      </ol>
    </nav>
    <div class="container">
      <form>
        <div class="form-group">
          <input class="form-control" type="text" v-model="post.title">
        </div>
        <div class="form-group">
          <textarea class="form-control" v-model="post.content"></textarea>
        </div>
        <div class="form-group images">
          <img :src="`https://leejinseok-blog.s3.ap-northeast-2.amazonaws.com/${image.s3_key}`" alt="" v-for="image in post.postImages" :key="image.id">
        </div>
        <div class="form-group">
          <button class="btn btn-primary" type="button" @click="update()">수정</button>
          <button class="btn btn-danger" type="button" @click="remove()">삭제</button>
        </div>
      </form> 
    </div>
  </div>
</template>

<script>
export default {
  layout: 'admin',
  async asyncData(context) {
    const { $axios, route } = context;
    const { data: post } = await $axios({
      url: `/api/v1/posts/${route.params.id}`,
      method: 'get'
    });

    return {
      post
    }
  },
  methods: {
    update: async function() {
      if (!confirm('정말 수정하시겠습니까?')) return;
      
      const data = this.post;
      try {
        const result = await this.$axios({
          url: `/api/v1/posts/${data.id}`,
          method: 'put',
          data
        });

        this.$router.push('/admin/posts');
      } catch (e) {
        console.log(e);
        console.log(e.message);
        alert('에러발생');
      }
    },
    remove: async function() {
      try {
        if (!confirm('정말 삭제하시겠습니까?')) return;

        const result = await this.$axios({
          url: `/api/v1/posts/${this.post.id}`,
          method: 'delete'
        });

        this.$router.push('/admin/posts');
      } catch (e) {
        console.log(e);
        alert('에러발생');
      }
    }
  }
}
</script>

<style scoped>
form {
  margin-left: auto;
  margin-right: auto;
  max-width: 600px;
  margin-top: 30px;
}

form input,
form textarea {
  display: block;
  width: 100%;
}

form textarea {
  margin-top: 10px;
}

form button {
  border: 1px solid #eaeaea;
  border-radius: 6px;
  margin-top: 6px;
}

.form-group.images img {
  max-width: 100%;
}
</style>