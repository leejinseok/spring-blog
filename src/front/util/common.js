import pagination from 'pagination';

let timer = null;
export function debounce(func, wait) {
  return function() {
    if (timer) {
      clearTimeout(timer);
    }

    timer = setTimeout(function() {
      func.apply(this);
    }.bind(this), wait);
  }
}

export function getPaginator(prelink, data) {
  return pagination.create('search', {
    prelink, 
    current: data.number + 1, 
    rowsPerPage: data.size, 
    totalResult: data.totalElements
  }).getPaginationData();
}