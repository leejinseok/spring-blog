<template>
  <div>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="#">관리자페이지</a></li>
        <li class="breadcrumb-item active"><nuxt-link to="/admin/posts">게시글</nuxt-link></li>
        <li class="breadcrumb-item active" aria-current="page">글쓰기</li>
        <LogoutBtn />
      </ol>
    </nav>
    <div class="container">
      <div class="form-container">
        <form @submit="submit($event)">
          <div class="form-group">
            <input class="form-control" v-model="title" maxlength="50" type="text" placeholder="타이틀">
          </div>
          <div class="form-group">
            <textarea class="form-control" v-model="content" placeholder="내용"></textarea>
          </div>
          <div class="form-group register-tag">
            <input class="form-control" type="text" v-model="postTagInputText" placeholder="태그" @keydown="handleKeyUpPostTagInputText">
            <button class="btn btn-sm btn-dark" type="button">추가</button>
          </div>
          <div class="form-group list-tag">
            <div class="tag-wrapper" v-for="(tag, tagIndex) in postTags" :key="tagIndex">
              <div class="close" title="삭제" @click="removeTag(tagIndex)">X</div>
              <span >
                {{ tag.text }}
              </span>
            </div>
          </div>
          <div class="form-group">
            <input class="form-control" type="file" ref="file">
          </div>
          <div class="form-group">
            <button type="submit" class="btn btn-primary">등록</button>     
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs';
import LogoutBtn from '~/components/admin/LogoutBtn';
import {addPost} from '~/api/posts';

export default {
  layout: 'admin',
  components: {
    LogoutBtn
  },
  data() {
    return {
      title: '',
      content: '',
      postTagInputText: '',
      postTags: [],
    }
  },
  methods: {
    submit: async function(event) {
      event.preventDefault();

      const data = new FormData();
      data.append('title', this.title);
      data.append('content', this.content);
      data.append('postTags', JSON.stringify(this.postTags));
      if (this.$refs.file.files[0]) {
        data.append('file', this.$refs.file.files[0]);
      }

      try {
        const result = await addPost.bind(this)(data);
        this.$router.push('/admin/posts');
      } catch (e) {
        console.log(e);
        alert('에러발생');
      }
    },
    handleKeyUpPostTagInputText: function($event) {
      $event.stopPropagation();
      if ($event.keyCode === 13) {
        $event.preventDefault();
        if (!this.postTags.some(item => item.text === this.postTagInputText)) {
          this.postTags.push({
            text: this.postTagInputText
          });
        };
        this.postTagInputText = '';
      }
    },
    removeTag: function(index) {
      this.postTags.splice(index, 1);
    }
  }
}
</script>

<style scoped>
.container {
  text-align: center;
}

.form-container {
  margin-left: auto;
  margin-right: auto;
}

.form-container input {
  margin-top: 30px;
}

.form-container input,
.form-container textarea {
  width: 100%;
}

.form-container textarea {
  margin-top: 10px;
}

.form-group.register-tag {
  display: flex;
  align-items: center;
}

.form-group.register-tag input,
.form-group.register-tag button {
  display: inline;
  width: auto;
  margin-top: 0;
}

.form-group.register-tag button {
  margin-left: 6px;
}

.form-group.list-tag {
  text-align: left;
}
</style>