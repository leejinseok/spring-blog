package com.wonder.blog.repository;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
  Collection<PostImage> findAllByPost(Post post);
}
