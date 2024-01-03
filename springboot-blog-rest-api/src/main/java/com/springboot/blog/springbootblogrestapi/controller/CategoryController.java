package com.springboot.blog.springbootblogrestapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

//import javax.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.springbootblogrestapi.entity.Category;
import com.springboot.blog.springbootblogrestapi.payload.CategoryDTO;
import com.springboot.blog.springbootblogrestapi.payload.CategoryResponse;
import com.springboot.blog.springbootblogrestapi.service.CategoryService;
import com.springboot.blog.springbootblogrestapi.utils.AppConstants;

@RestController
@RequestMapping("/api/categories")
@Tag(
        name = "CRUD APIs for Category Resource",
        description = "APIs for managing categories"
)
public class CategoryController {
    
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //add a new category by ADMIN
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Add a new category",
            description = "Create a new category"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Category created successfully."
    )
    public ResponseEntity<CategoryDTO> createPost(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

    //get all the posts tagged to all categories - available only for Admin
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    @Operation(
            summary = "Get all categories with posts.",
            description = "Retrieve all categories with associated posts, available only for admins."
    )
    public CategoryResponse getAllCategories(
        @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return categoryService.getAllCategoriesWithPosts(pageNo, pageSize, sortBy, sortDir);
    }

    //get all the list of categories available
    // @GetMapping
    // public ResponseEntity<List<Category>> getAllCategories() {
    //     return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    // }
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Retrieve a list of all available categories."
    )
    public ResponseEntity<List<Map<String, Object>>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    //get all the posts available for a particular category - only for ADMIN
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{categoryId}")
    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a category by its ID, available only for admins."
    )
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable(name = "categoryId") long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    //update a category by id
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    @Operation(
            summary = "Update a category by its ID",
            description = "Update a category by its ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post updated successfully."
    )
    public ResponseEntity<CategoryDTO> updateCategoryById(@Valid @RequestBody CategoryDTO categoryDTO,
            @PathVariable(name = "categoryId") long categoryId,
            @RequestParam(required = false) Set<String> fieldsToUpdate) {
        CategoryDTO categoryResponse = categoryService.updateCategory(categoryDTO, categoryId, fieldsToUpdate);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    //delete a category by id
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    @Operation(
            summary = "Delete a category by its id",
            description = "Delete a category by its ID, available only for admins."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post deleted successfully."
    )
    public ResponseEntity<String> deleteCategoryById(@PathVariable(name = "categoryId") long categoryId) {
        categoryService.deleteByCategoryId(categoryId);
        return new ResponseEntity<>("Category deleted successfully!!!", HttpStatus.OK);
    }
}