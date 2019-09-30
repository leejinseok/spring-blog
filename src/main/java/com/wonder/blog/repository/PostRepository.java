package com.wonder.blog.repository;

import com.wonder.blog.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
