package com.wonder.blog.repository;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
}
