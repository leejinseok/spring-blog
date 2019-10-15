<script>
export default {
  async asyncData(context) {
    const { route, redirect } = context;
    redirect('/posts');
  }
}
</script>

<style>

</style>