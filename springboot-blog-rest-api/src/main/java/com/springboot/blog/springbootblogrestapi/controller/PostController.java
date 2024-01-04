package com.springboot.blog.springbootblogrestapi.controller;

import java.util.List;
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
import com.springboot.blog.springbootblogrestapi.payload.PostDTO;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;
import com.springboot.blog.springbootblogrestapi.service.PostService;
import com.springboot.blog.springbootblogrestapi.utils.AppConstants;

@RestController
//@RequestMapping("/api/")
@RequestMapping("/api/v1/")
//Changing the version of Post APIs as the endpoints have changed. Now in order to create a post we need
//to mention the category under which the post will be created.
@Tag(
        name = "CRUD APIs for Post Resource",
        description = "APIs for managing posts"
)
public class PostController {
    
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories/{categoryName}/posts")
    @Operation(
            summary = "Create a new post"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Post created successfully"
    )
    public ResponseEntity<PostDTO> createPost(@PathVariable(value = "categoryName") String categoryName,
                          @Valid @RequestBody PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(categoryName, postDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/categories/{categoryName}/posts")
    @Operation(
            summary = "Get all the posts tagged to a category",
            description = "This API used by admin retrieves all the posts in the specified category."
    )
    public List<PostDTO> getPostsByCategories(@PathVariable(value = "categoryName") String categoryName) {
        return postService.getAllPostsByCategoryName(categoryName);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/categories/{categoryName}/posts/{postId}")
    @Operation(
            summary = "Get post by ID",
            description = "This API to be used by admin retrieves a post by its ID in the specified category."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post retrieved succesfully."
    )
    public ResponseEntity<PostDTO> getPostById(
        @PathVariable(value = "categoryName") String categoryName,
        @PathVariable(value = "postId") Long postId
    ) {
        PostDTO postDTO = postService.getPostById(categoryName, postId);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories/{categoryName}/posts/{postId}")
    @Operation(
            summary = "Update a post by its ID.",
            description = "This post updates a post by its ID in the specified category."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post updated successfully."
    )
    public ResponseEntity<PostDTO> updatePost(
        @PathVariable(value = "categoryName") String categoryName,
        @PathVariable(value = "postId") Long postId,
        @Valid @RequestBody PostDTO postDTO,
        @RequestParam(required = false) Set<String> fieldsToUpdate
    ) {
        PostDTO updatedPost = postService.updatePost(categoryName, postId, postDTO, fieldsToUpdate);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @DeleteMapping("/categories/{categoryName}/posts/{postId}")
    @Operation(
            summary = "Delete a post by its ID",
            description = "This API deletes a post by its ID in the specified category."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post deleted successfully."
    )
    public ResponseEntity<String> deletePost(
        @PathVariable(value = "categoryName") String categoryName,
        @PathVariable(value = "postId") Long postId
    ) {
        postService.deletePost(categoryName, postId);
        return new ResponseEntity<>("Post deleted successfully!!!", HttpStatus.OK);
    }



    // //create blog post
    // @PreAuthorize("hasRole('ADMIN')")
    // @PostMapping
    // public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
    //     return new ResponseEntity<>(postService.createPostDTO(postDTO), HttpStatus.CREATED);
    // }

    // //get all posts rest api without Pagination and Sorting Support
    // //@GetMapping
    // //public List<PostDTO> getAllPostDTOs() {
    // //    return postService.getAllPosts();
    // //}

    // //get all posts rest api with Pagination and Sorting support
    // @GetMapping
    // public PostResponse getAllPostsDTOs(
    //     @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
    //     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
    //     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
    //     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    // ){
    //     return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    // }

    // //get post by id
    // @GetMapping("/{id}")
    // public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") long id) {
    //     return ResponseEntity.ok(postService.getPostById(id));
    // }

    // //update post by id
    // @PreAuthorize("hasRole('ADMIN')")
    // @PutMapping("/{id}")
    // public ResponseEntity<PostDTO> updatePostById(@Valid @RequestBody PostDTO postDTO, 
    //                     @PathVariable(name = "id") long id,
    //                     @RequestParam(required = false) Set<String> fieldsToUpdate) {
    //     PostDTO postResponse = postService.updatePost(postDTO, id, fieldsToUpdate);
    //     return new ResponseEntity<>(postResponse, HttpStatus.OK);
    // }

    // //delete post by id
    // @PreAuthorize("hasRole('ADMIN')")
    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deletePostById(@PathVariable(name = "id") long id) {
    //     postService.deletePostById(id);
    //     return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
    // }
}
