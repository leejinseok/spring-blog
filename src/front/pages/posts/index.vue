<template>
  <div class="container">
    <Header />
    <div class="container__inner">

      <div class="search-container">
        <img :src="searchSvg" class="search-icon">
        <input type="text" v-model="q" placeholder="검색어를 입력해주세요." @input="handleInputSearch($event)">
      </div>

      <div class="post-container">
        <ul v-if="posts.length">
          <li v-for="post in posts" :key="post.id">
            <div class="title-and-date">
              <p class="title">
                <nuxt-link :to="`/posts/${post.uuid}`">
                  {{ post.title }}
                </nuxt-link>
              </p>
              <p class="createdAt">
                {{ displayDate(post.createdAt) }} 작성
              </p>
            </div>
          </li>
        </ul>
        <div v-else class="no-data">
          <h3>게시글이 존재하지 않습니다...</h3>
        </div>
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
import { findPosts } from '~/api/posts';
import { mapState } from 'vuex';
import pagination from 'pagination';
import searchSvg from '~/assets/fontawesome-free-5.11.2-web/svgs/solid/search.svg';
import { debounce, getPaginator } from '~/util/common';

export default {
  components: {
    Header,
  },
  computed: {
    ...mapState({
      posts: state => state.posts.data,
      paginator: state => state.posts.paginator
    })
  },
  async asyncData(context) {
    const { store, route, $axios } = context;
    const result = await findPosts(route.query, $axios);
    const paginator = getPaginator('/posts', result.data);
    store.commit('posts/set', {
      data: result.data.content,
      paginator
    });
  },
  data() {
    return {
      q: '',
      searchSvg
    }
  },
  watch: {
    '$route': function() {
      this.fetchPosts();
    },
  },
  methods: {
    handleInputSearch: function(evt) {
      this.q = evt.target.value;
      this.fetchPosts();
    },
    fetchPosts: debounce(async function() {
      const params = Object.assign(this.$route.query, { q: this.q });
      const result = await findPosts(params, this.$axios);
      const paginator = getPaginator('/posts', result.data);
      this.$store.commit('posts/set', {
        data: result.data.content,
        paginator
      });
    }, 300),
    displayDate: function (val) {
      const date = new Date(val);
      return date.getFullYear() + "년 " + (+date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + "시 " + date.getMinutes() + "분";
    },

  }
}
</script>

<style scoped>
.container__inner .search-container {
  margin-bottom: 16px;
  position: relative;
}

.container__inner .search-container .search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  max-width: 26px;
  width: 100%;
}

.container__inner .search-container input {
  width: 100%;
  padding: 20px 10px;
  padding-left: 60px;
  box-sizing: border-box;
  border: 0;
  font-size: 24px;
}

.container__inner .post-container ul li {
  padding-top: 20px;
  padding-bottom: 20px;
  border-top: 2px solid #eaeaea;
}

.container__inner .post-container .no-data h3 {
  text-align: center;
  font-size: 22px;
}

.title-and-date {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 18px;
}

.title-and-date .title {
}

.title-and-date .createdAt {
  font-size: 16px;
  color: #555;
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
