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

      <div class="tags">
        <span v-for="tag in post.postTags" :key="tag.id">
          #{{ tag.text }}
        </span>
      </div>

      <div class="images" v-if="post.postImages.length">
        <div class="wrapper-image" v-for="postImage in post.postImages" :key="postImage.id">
          <img :src="postImage.url" alt="">
        </div>
      </div>

      <div class="date">
        <p class="user">{{ post.user.name }}</p>
        <p class="createdAt">{{ displayDate(post.createdAt) }} 작성</p>
        <p class="updatedAt" v-if="post.udpatedAt">{{ displayDate(post.updatedAt) }} 최종수정</p>
      </div>

      <div id="disqus_thread"></div>
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
  mounted: function() {
    this.initDisqus();
  },
  methods: {
    displayDate,
    initDisqus: function() {
      var disqus_config = function () {
        this.page.url = 'localhost';  // Replace PAGE_URL with your page's canonical URL variable
        this.page.identifier = ''; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
      };
      (function() { // DON'T EDIT BELOW THIS LINE
        var d = document, s = d.createElement('script');
        s.src = 'https://leejinseok-io.disqus.com/embed.js';
        s.setAttribute('data-timestamp', +new Date());
        (d.head || d.body).appendChild(s);
      })();
    }
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

.tags {
  margin-top: 12px;
}

.tags span {
  padding: 5px 8px;
  background-color: #aaa;
  color: #fff;
  border-radius: 6px;
  font-size: 12px;
  box-sizing: border-box;
  display: inline-block;
  margin-right: 4px;
  margin-bottom: 4px;
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