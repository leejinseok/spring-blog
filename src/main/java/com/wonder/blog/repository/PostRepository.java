package com.wonder.blog.repository;

import com.wonder.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

  @Override
  Optional<Post> findById(Integer id);

  Optional<Post> findByUuid(String uuid);

  @Override
  Page<Post> findAll(Pageable pageable);

  @Query("select p from Post p where (p.title like CONCAT('%', :q, '%'))")
  Page<Post> findPosts(Pageable pageable, @Param("q") String title);

  void deleteByUuid(String uuid);
}
