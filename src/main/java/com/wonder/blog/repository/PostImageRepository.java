package com.wonder.blog.repository;

import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
  Collection<PostImage> findAllByPost(Post post);
}
