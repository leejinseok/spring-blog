<template>
  <div class="container">
    <Header />
    <div class="container__inner">
      <div class="content">
        {{ post.content }}
      </div>
      <div class="date">
        <p class="createdAt">{{ displayDate(post.createdAt) }} 작성</p>
        <p class="updatedAt">{{ displayDate(post.updatedAt) }} 최종수정</p>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '~/components/Header/Header';
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
.content {
  white-space: pre-line;
  font-size: 18px;
  line-height: 1.5;
  border-top: 2px solid #eaeaea;
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