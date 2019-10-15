<template>
  <div class="container">
    <Header />
    <div class="container__inner">
      {{ post.content }}
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
  }
}
</script>

<style>

</style>