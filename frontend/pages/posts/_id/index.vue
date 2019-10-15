<template>
  <div class="container">
    <Header />
    <div class="container__inner">
      <div class="content">
        {{ post.content }}
      </div>
    </div>
  </div>
</template>

<script>
import Header from '~/components/Header/Header';

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
    nl2br: function(str) {
      return str.replace(/\n/g, '<br>');
    }
  }
}
</script>

<style scoped>
.content {
  white-space: pre-line;
  font-size: 18px;
  line-height: 1.5;
}
</style>