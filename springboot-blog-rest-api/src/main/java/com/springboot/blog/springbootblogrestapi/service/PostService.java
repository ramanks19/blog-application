package com.springboot.blog.springbootblogrestapi.service;

import com.springboot.blog.springbootblogrestapi.payload.PostDTO;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;

import java.util.List;
import java.util.Set;

public interface PostService {
//     PostDTO createPostDTO(PostDTO postDTO);

// //    List<PostDTO> getAllPosts(int pageNo, int pageSize);
//     PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

//     PostDTO getPostById(long id);

//     PostDTO updatePost(PostDTO postDTO, long id, Set<String> fieldsToUpdate);

//     void deletePostById(long id);

    PostDTO createPost(String categoryName, PostDTO postDTO);

    List<PostDTO> getAllPostsByCategoryName(String categoryName);

    PostDTO getPostById(String categoryName, Long postId);

    PostDTO updatePost(String categoryName, Long postId, PostDTO postDTO, Set<String> fieldsToUpdate);

    void deletePost(String categoryName, Long postId);

    
}
