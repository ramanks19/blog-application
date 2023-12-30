package com.springboot.blog.springbootblogrestapi.service;

import java.util.List;
import java.util.Set;

import com.springboot.blog.springbootblogrestapi.payload.CommentDTO;

public interface CommentService {
    CommentDTO createComment(Long postId, CommentDTO commentDTO);
    
    List<CommentDTO> getCommentsByPostId(long postId);

    CommentDTO getCommentById(long postId, long commentId);

    CommentDTO updateCommentById(long postId, long commentId, CommentDTO commentDTO, Set<String> fieldsToUpdate);

    void deleteComment(long postId, long commentId);
}
