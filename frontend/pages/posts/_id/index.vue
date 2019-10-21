<template>
  <div class="container">
    <Header />
    <div class="container__inner">
      <div class="title">
        <h3>{{ post.title }}</h3>
      </div>

      <div class="content">
        {{ post.content }}
      </div>

      <div class="images">
        <div class="wrapper-image" v-for="postImage in post.postImages" :key="postImage.id">
          <img :src="postImage.url" alt="">
        </div>
      </div>

      <div class="date">
        <p class="user">{{ post.user.name }}</p>
        <p class="createdAt">{{ displayDate(post.createdAt) }} 작성</p>
        <p class="updatedAt">{{ displayDate(post.updatedAt) }} 최종수정</p>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '~/components/Header';
import { displayDate } from '~/plugins/util/date';

export default {
  components: {
    Header
  },
  async asyncData(context) {
    const { $axios, route } = context;
    const id = route.params.id;
    const { data: post } = await $axios({
      url: `/api/v1/posts/${id}`,
      method: 'get'
    });

    return {
      post
    };
  },
  methods: {
    displayDate
  }
}
</script>

<style scoped>
.title {
  border-top: 2px solid #eaeaea;
}

.title h3 {
  font-size: 22px;
  margin-top: 20px;
}

.content {
  white-space: pre-line;
  font-size: 18px;
  line-height: 1.5;
}

.images {
  margin-top: 6px;
}

.date {
  margin-top: 10px;
  color: #aaa;
  font-size: 14px;
}

.date p {
  margin-bottom: 4px;
}
</style>