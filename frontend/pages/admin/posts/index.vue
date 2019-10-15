<template>
  <div class="container">
    <h2>관리자페이지 - 게시글</h2>

    <div class="table-container">
      <table>
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
      <nuxt-link to="/admin/posts/write" class="btn btn-write">
        글쓰기
      </nuxt-link>
    </div>
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

.table-container {
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 30px;
}

.table-container .btn-write {
  border: 1px solid #aaa;
  border-radius: 5px;
  padding: 3px;
  margin-top: 4px;
  font-size: 14px;
}

.table-container table {
  width: 100%;
}

.table-container table td.createdAt {
  text-align: right;
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