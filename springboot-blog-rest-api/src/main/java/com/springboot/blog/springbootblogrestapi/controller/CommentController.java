package com.springboot.blog.springbootblogrestapi.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.springbootblogrestapi.payload.CommentDTO;
import com.springboot.blog.springbootblogrestapi.service.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {
    
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId, 
                @Valid @RequestBody CommentDTO commentDTO) {
        System.out.println(commentDTO);
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId") Long postId,
                @PathVariable(value = "commentId") Long commentId) {
        CommentDTO commentDTO = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId") Long postId,
                @PathVariable(value = "commentId") Long commentId,
                @Valid @RequestBody CommentDTO commentDTO,
                @RequestParam(required = false) Set<String> fieldsToUpdate) {
        CommentDTO updatedComment = commentService.updateCommentById(postId, commentId, commentDTO, fieldsToUpdate);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
