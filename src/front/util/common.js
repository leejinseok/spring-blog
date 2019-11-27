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