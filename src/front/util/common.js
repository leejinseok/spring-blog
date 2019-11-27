let timer = null;
export function debounce(func, wait) {

  if (typeof func != 'function') {
    throw new TypeError('First arg must function type');
  }

  function debounced() {
    if (timer) {
      clearTimeout(timer);
    }
  
    timer = setTimeout(func, wait);
  }
  
  return debounced;
}