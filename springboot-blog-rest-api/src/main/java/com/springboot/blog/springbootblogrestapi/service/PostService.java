package com.springboot.blog.springbootblogrestapi.service;

import com.springboot.blog.springbootblogrestapi.payload.PostDTO;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDTO createPostDTO(PostDTO postDTO);

//    List<PostDTO> getAllPosts(int pageNo, int pageSize);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDTO getPostById(long id);

    PostDTO updatePost(PostDTO postDTO, long id);

    void deletePostById(long id);
    
}
