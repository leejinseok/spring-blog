<template>
  <div>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="#">관리자페이지</a></li>
        <li class="breadcrumb-item active" aria-current="page">게시글</li>
      </ol>
    </nav>
    
    <div class="container">
      <div class="table-container">
        <table class="table">
          <thead>
            <th>No.</th>
            <th>타이틀</th>
            <th class="createdAt">등록일</th>
          </thead>
          <tbody>
            <tr v-for="post in posts" :key="post.id">
              <td>
                {{ post.id }}
              </td>
              <td>
                <nuxt-link :to="`/admin/posts/${post.id}`">
                  {{ post.title }}
                </nuxt-link>
              </td>
              <td class="createdAt">
                {{ post.createdAt }}
              </td>
            </tr>

          </tbody>
        </table>
        <nuxt-link to="/admin/posts/register" class="btn btn-primary">
          글쓰기
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  layout: 'admin',
  middleware: 'authenticated',
  async asyncData(context) {
    const { $axios, route } = context;
    const size = route.query.size || 10;
    const page = route.query.page || 0;
    const sort = route.query.sort || 'createdAt,desc';

    const { data: posts } = await $axios({
      url: '/api/v1/posts',
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
  }
}
</script>

<style>
h2 {
  text-align: center;
  font-size: 24px;
  margin-top: 50px;
}

.table-container {
  margin-top: 14px;
}
</style>