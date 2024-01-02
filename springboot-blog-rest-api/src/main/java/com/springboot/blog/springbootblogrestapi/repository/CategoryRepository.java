package com.springboot.blog.springbootblogrestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.springbootblogrestapi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String category);
}
