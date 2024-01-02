package com.springboot.blog.springbootblogrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.springbootblogrestapi.entity.Category;
import com.springboot.blog.springbootblogrestapi.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByCategory(Category category);
}
