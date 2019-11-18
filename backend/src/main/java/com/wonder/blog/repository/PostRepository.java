package com.wonder.blog.repository;

import com.wonder.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

  @Override
  Optional<Post> findById(Integer id);

  @Override
  Page<Post> findAll(Pageable pageable);
}
