export async function findAll ({size = 10, page = 1, sort = 'createdAt,desc'}, $axios) {
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
