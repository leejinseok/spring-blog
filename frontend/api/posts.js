import pagination from 'pagination';

export default async function (store, route, $axios) {
  const size = route.query.size || 10;
  const page = route.query.page || 1;
  const sort = route.query.sort || 'createdAt,desc';

  try {
    const result = await $axios({
      url: `/api/v1/posts`,
      method: 'get',
      params: {
        size,
        page: page - 1,
        sort
      }
    });

    const paginator = pagination.create('search', {
      prelink:'/posts', 
      current: result.data.number + 1, 
      rowsPerPage: result.data.size, 
      totalResult: result.data.totalElements
    });

    store.commit('posts/set', {
      data: result.data.content, 
      paginator: paginator.getPaginationData()
    });
  } catch (err) {
    console.error(err);
  }
}
