<template>
  <div class="container">
    <Header />
    <div class="container__inner">
      <ul>
        <li v-for="post in posts" :key="post.id">
          <div class="title-and-date">
            <p class="title">
              <nuxt-link :to="`/posts/${post.id}`">
                {{ post.title }}
              </nuxt-link>
            </p>
            <p class="createdAt">
              {{ displayDate(post.createdAt) }} 작성
            </p>
          </div>
        </li>
      </ul>
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
    const size = route.query.size || 10;
    const page = route.query.page || 0;
    const sort = route.query.sort || 'createdAt,desc';

    const { data: posts } = await $axios({
      url: `/api/v1/posts`,
      method: 'get',
      params: {
        size,
        page,
        sort
      }
    });

    return {
      posts
    }
  },
  data() {
  },
  mounted: async function() {
    console.log(this.$route);
  },
  methods: {
    displayDate: function (val) {
      const date = new Date(val);
      return date.getFullYear() + "년 " + (+date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + '시 ' + date.getMinutes() + '분';
    }
  }
}
</script>

<style scoped>
.container__inner ul li {
  padding-top: 20px;
  padding-bottom: 20px;
  border-top: 2px solid #eaeaea;
}

.title-and-date {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 18px;
}
</style>
