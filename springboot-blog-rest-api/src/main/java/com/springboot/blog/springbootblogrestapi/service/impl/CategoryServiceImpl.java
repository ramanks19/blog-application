package com.springboot.blog.springbootblogrestapi.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.springbootblogrestapi.entity.Category;
import com.springboot.blog.springbootblogrestapi.entity.Post;
import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.payload.CategoryDTO;
import com.springboot.blog.springbootblogrestapi.payload.CategoryResponse;
import com.springboot.blog.springbootblogrestapi.payload.PostDTO;
import com.springboot.blog.springbootblogrestapi.repository.CategoryRepository;
import com.springboot.blog.springbootblogrestapi.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = mapToEntity(categoryDTO);
        Category newCategory = categoryRepository.save(category);
        CategoryDTO categoryResponse = mapToDTO(newCategory);

        return categoryResponse;
    }

    @Override
    public CategoryResponse getAllCategoriesWithPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Category> categories = categoryRepository.findAll(pageable);
        //get content for page object
        // List<Category> listOfCategories = categories.getContent();
        // List<CategoryDTO> content = listOfCategories.stream().map(category -> mapToDTO(category)).collect(Collectors.toList());
        List<CategoryDTO> content = categories.getContent().stream().map(
            category -> {
                CategoryDTO categoryDTO = mapToDTO(category);
                List<PostDTO> postDTOs = category.getPosts().stream()
                                        .map(post -> mapPostToDTO(post))
                                        .collect(Collectors.toList());
                categoryDTO.setPosts(postDTOs);
                return categoryDTO;
            }
        ).collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(content);
        categoryResponse.setPageNo(categories.getNumber());
        categoryResponse.setPageSize(categories.getSize());
        categoryResponse.setTotalElements(categories.getTotalElements());
        categoryResponse.setTotalPages(categories.getTotalPages());
        categoryResponse.setLast(categories.isLast());

        return categoryResponse;
    }

    // @Override
    // public List<Category> getAllCategories() {
    //     return categoryRepository.findAll();
    // }
    @Override
    public List<Map<String, Object>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::mapToSummaryMap).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        
        return mapToDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, long categoryId, Set<String> fieldsToUpdate) {
        Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        
        Set<String> validFields = Arrays.stream(Category.class.getDeclaredFields())
                                        .map(Field::getName)
                                        .collect(Collectors.toSet());
        if (!validFields.containsAll(fieldsToUpdate)) {
            Set<String> invalidFields = new HashSet<>(fieldsToUpdate);
            invalidFields.removeAll(validFields);
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid fields: " + invalidFields);
        }

        if (fieldsToUpdate.contains("name")) {
            category.setName(categoryDTO.getName());
        }
        if (fieldsToUpdate.contains("description")) {
            category.setDescription(categoryDTO.getDescription());
        }

        Category updatedCategory = categoryRepository.save(category);
        return mapToDTO(updatedCategory);
    }

    @Override
    public void deleteByCategoryId(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
    }

    //Convert DTO to entity
    private Category mapToEntity(CategoryDTO categoryDTO){
        Category category = modelMapper.map(categoryDTO, Category.class);
        return category;
    }

    //Convert entity to DTO
    private CategoryDTO mapToDTO(Category category){
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    //Convert Post entity to PostDTO
    private PostDTO mapPostToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

    private Map<String, Object> mapToSummaryMap(Category category) {
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("name", category.getName());
        categoryMap.put("description", category.getDescription());
        return categoryMap;
    }
    
}
