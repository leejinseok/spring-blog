export async function findPosts({size = 10, page = 1, sort = 'createdAt,desc'}, $axios) {
  return $axios({
    url: `/api/v1/posts`,
    method: 'get',
    params: {
      size,
      page: page - 1,
      sort
    }
  });
}

export async function addPost(data) {
  return this.$axios({
    url: '/api/v1/posts',
    method: 'post',
    header: {
      'Content-Type': 'multipart/form-data'
    },
    data
  });
}

export async function updatePost(data) {
  return this.$axios({
    url: `/api/v1/posts/${data.id}`,
    method: 'put',
    data
  });
}