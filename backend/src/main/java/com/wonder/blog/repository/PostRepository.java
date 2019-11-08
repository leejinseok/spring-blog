package com.wonder.blog.repository;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

  @Override
  Optional<Post> findById(Integer integer);

  @Override
  Page<Post> findAll(Pageable pageable);
}
