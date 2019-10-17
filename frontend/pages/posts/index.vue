<template>
  <div class="container">
    <Header />
    <div class="container__inner">
      <div class="post-container">
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

      <div class="pagination-container" v-if="paginator.range.length">
        <ul class="clearfix" >
          <li :class="{disabled: !paginator.previous}">
            <nuxt-link :to="`${paginator.prelink}?page=${(paginator.previous)}&size=10`" v-if="paginator.previous !== null">
              «
            </nuxt-link>
            <a v-else class="disabled">
              «
            </a>
          </li>

          <li v-for="(item, index) in paginator.range" :key="index" :class="{active: item === paginator.current}">
            <nuxt-link :to="{path: item.prefix, query: {page: item}}">
              {{ item }}
            </nuxt-link>
          </li>
           <li :class="{disabled: !paginator.next}">
            <nuxt-link :to="`${paginator.prelink}?page=${(paginator.next)}&size=10`" v-if="paginator.next !== null">
              »
            </nuxt-link>
            <a v-else class="disabled">
              »
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '~/components/Header';
import getPosts from '~/api/posts';
import { mapState } from 'vuex';

export default {
  components: {
    Header
  },
  computed: {
    ...mapState({
      posts: state => state.posts.data,
      paginator: state => state.posts.paginator
    })
  },
  async asyncData(context) {
    const { store, route, $axios } = context;
    await getPosts(store, route, $axios);
  },
  data() {
  },
  watch: {
    '$route': function() {
      this.fetchPosts();
    }
  },
  mounted: async function() {
  },
  methods: {
    displayDate: function (val) {
      const date = new Date(val);
      return date.getFullYear() + "년 " + (+date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + '시 ' + date.getMinutes() + '분';
    },
    fetchPosts: async function() {
      const { $store, $route, $axios } = this;
      await getPosts($store, $route, $axios);
    }
  }
}
</script>

<style scoped>
.container__inner .post-container ul li {
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

.pagination-container {
  margin-top: 8px;
}

.pagination-container ul li {
  float: left;
  border: 1px solid #aaa;
  border-right: none;
}

.pagination-container ul li:first-child,
.pagination-container ul li:first-child a {
  border-radius: 3px 0 0 3px;
}

.pagination-container ul li:last-child {
  border-right: 1px solid #aaa;
  border-radius: 0px 3px 3px 0px;
}

.pagination-container ul li a {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 36px;
  width: 46px;
  color: #333;
}

.pagination-container ul li a:hover {
  text-decoration: none;
  color: #222;
}

.pagination-container ul li a.disabled {
  opacity: 0.3;
  cursor:default;
}

.pagination-container ul li.active a {
  background: #aaa;
  color: #fff;
}
</style>
