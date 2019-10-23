<template>
  <div>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="#">관리자페이지</a></li>
        <li class="breadcrumb-item active"><nuxt-link to="/admin/posts">게시글</nuxt-link></li>
        <li class="breadcrumb-item active" aria-current="page">{{ post.title }}</li>
        <LogoutBtn />
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
          <div class="wrapper-image" v-for="(postImage, index) in post.postImages" :key="postImage.id">
            <button type="button" @click="removeImage(postImage, index)" title="이미지삭제">X</button>
            <img :src="postImage.url">
          </div>

          <input type="file" ref="file" @change="uploadImage">
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
import LogoutBtn from '~/components/admin/LogoutBtn';

export default {
  layout: 'admin',
  components: {
    LogoutBtn
  },
  async asyncData(context) {
    const { $axios, route, redirect } = context;

    try {
      const { data: post } = await $axios({
        url: `/api/v1/posts/${route.params.id}`,
        method: 'get'
      });
      return {
        post
      }
    } catch (e) {
      console.error(e.message);
      redirect('/admin/posts');
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
    },
    removeImage: async function(image, index) {
      try {
        const result = await this.$axios({
          url: `/api/v1/posts/images/${image.id}`,
          method: 'delete',
        });

        this.post.postImages.splice(index, 1);
      } catch (e) {
        alert('에러발생!');
        console.log(e);
      }
    },
    uploadImage: async function() {
      try {
        const file = this.$refs.file.files[0];
        const formData = new FormData();
        formData.append('file', file);
        const result = await this.$axios({
          url: `/api/v1/posts/${this.post.id}/images`,
          method: 'put',
          data: formData
        });

        this.post.postImages.push(result.data);
        this.$refs.file.form.reset();
      } catch (e) {
        alert('에러발생');
        console.error(e);
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
  min-height: 200px;
}

form button {
  border: 1px solid #eaeaea;
  border-radius: 6px;
  margin-top: 6px;
}

.form-group.images {
  position: relative;
}

.form-group.images .wrapper-image {
  position: relative;
}

.form-group.images img {
  max-width: 100%;
}

.form-group.images button {
  position: absolute;
  right: 4px;
  top: 4px;
  margin-top: 0;
  width: 20px;
  height: 20px;
  font-size: 12px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, .5);
  color: #fff;
  border: 0;
}

.form-group [type=file] {
  margin-top: 6px;
}
</style>