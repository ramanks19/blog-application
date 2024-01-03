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
@Tag(
        name = "CRUD APIs for Comment resource",
        description = "APIs for managing comments"
)
public class CommentController {
    
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PostMapping("/posts/{postId}/comments")
    @Operation(
            summary = "Create a new comment",
            description = "Create a new comment for a specific blog post."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment created successfully."
    )
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId, 
                @Valid @RequestBody CommentDTO commentDTO) {
        System.out.println(commentDTO);
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(
            summary = "Get comments by post ID",
            description = "Retrieve all comments for a specific blog post"
    )
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    @Operation(
            summary = "Get comment by post and comment ID",
            description = "Retrieve a comment by post ID and comment ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment retrieved successfully."
    )
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId") Long postId,
                @PathVariable(value = "commentId") Long commentId) {
        CommentDTO commentDTO = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PutMapping("/posts/{postId}/comments/{commentId}")
    @Operation(
            summary = "Update a comment",
            description = "Update a comment by post and comment ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment updated successfully"
    )
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId") Long postId,
                @PathVariable(value = "commentId") Long commentId,
                @Valid @RequestBody CommentDTO commentDTO,
                @RequestParam(required = false) Set<String> fieldsToUpdate) {
        CommentDTO updatedComment = commentService.updateCommentById(postId, commentId, commentDTO, fieldsToUpdate);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment by post ID and comment ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment deleted successfully"
    )
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
