<template>
  <div class="container">
    <h2>관리자페이지 - 게시글</h2>

    <table>
      <thead>
        <th>No.</th>
        <th>타이틀</th>
        <th>등록일</th>
      </thead>
      <tbody>
        <tr v-for="post in posts" :key="post.id">
          <td>
            {{ post.id }}
          </td>
          <td>
            {{ post.title }}
          </td>
          <td>
            {{ post.createdAt }}
          </td>
        </tr>

      </tbody>
    </table>
  </div>
</template>

<script>
export default {
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

table {
  margin-left: auto;
  margin-right: auto;
  margin-top: 20px;
  border-collapse: collapse;
}

table td,
table th {
  border: 1px solid #aaa;
  padding: 10px;
}
</style>