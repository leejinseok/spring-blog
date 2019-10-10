package com.wonder.blog.repository;

import com.wonder.blog.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
  List<Post> findAll();
}
