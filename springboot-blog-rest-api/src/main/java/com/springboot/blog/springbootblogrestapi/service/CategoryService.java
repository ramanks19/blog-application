package com.springboot.blog.springbootblogrestapi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.springboot.blog.springbootblogrestapi.entity.Category;
import com.springboot.blog.springbootblogrestapi.payload.CategoryDTO;
import com.springboot.blog.springbootblogrestapi.payload.CategoryResponse;

public interface CategoryService {
    
    CategoryDTO addCategory(CategoryDTO categoryDTO);

    CategoryResponse getAllCategoriesWithPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    CategoryDTO getCategoryById(long categoryId);

    List<Map<String, Object>> getAllCategories();

    CategoryDTO updateCategory(CategoryDTO categoryDTO, long categoryId, Set<String> fieldsToUpdate);

    void deleteByCategoryId(long categoryId);
}
