package com.springboot.blog.springbootblogrestapi.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.springbootblogrestapi.entity.Comment;
import com.springboot.blog.springbootblogrestapi.entity.Post;
import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.payload.CommentDTO;
import com.springboot.blog.springbootblogrestapi.repository.CommentRepository;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import com.springboot.blog.springbootblogrestapi.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);
        //Convert post entity by id
        Post post = getPostByIdOrThrow(postId);
        //set post to entity
        comment.setPost(post);
        //comment entity to DB
        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        //retrieve comments by post id
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert list of comment entities to list of comment DTO
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        //retrieve post by post Id
        Post post = getPostByIdOrThrow(postId);
        //retrieve comment by comment id
        Comment comment = getCommentByIdOrThrow(commentId);
        validateCommentBelongsToPost(comment, post);

        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateCommentById(long postId, long commentId, CommentDTO commentDTO, Set<String> fieldsToUpdate) {
        Post post = getPostByIdOrThrow(postId);
        Comment comment = getCommentByIdOrThrow(commentId);
        validateCommentBelongsToPost(comment, post);

        Set<String> validFields = Arrays.stream(Comment.class.getDeclaredFields())
                                        .map(Field::getName)
                                        .collect(Collectors.toSet());
        
        if (!validFields.containsAll(fieldsToUpdate)) {
            Set<String> invalidFields = new HashSet<>(fieldsToUpdate);
            invalidFields.removeAll(validFields);
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid fields are: " + invalidFields);
        }

        //Update only the specified fields
        if (fieldsToUpdate.contains("name")) {
            comment.setName(commentDTO.getName());
        }
        if (fieldsToUpdate.contains("email")) {
            comment.setEmail(commentDTO.getEmail());
        }
        if (fieldsToUpdate.contains("body")) {
            comment.setBody(commentDTO.getBody());
        }

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = getPostByIdOrThrow(postId);
        Comment comment = getCommentByIdOrThrow(commentId);
        validateCommentBelongsToPost(comment, post);

        commentRepository.delete(comment);
    }

    //Convert entity to DTO
    private CommentDTO mapToDTO(Comment comment) {
        // CommentDTO commentDTO = new CommentDTO();
        // commentDTO.setId(comment.getId());
        // commentDTO.setName(comment.getName());
        // commentDTO.setEmail(comment.getEmail());
        // commentDTO.setBody(comment.getBody());
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        return commentDTO;
    }

    //Convert DTO to entity
    private Comment mapToEntity(CommentDTO commentDTO) {
        // Comment comment = new Comment();
        // comment.setName(commentDTO.getName());
        // comment.setEmail(commentDTO.getEmail());
        // comment.setBody(commentDTO.getBody());
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        return comment;
    }

    //Check whether it is a valid post id
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new ResourceNotFoundException("Post", "postId", postId)
        );
    }

    //Check whether it is a valid comment id
    private Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new ResourceNotFoundException("Comment", "commentId", commentId)
        );
    }

    //Check whether a comment belongs to a post
    private void validateCommentBelongsToPost(Comment comment, Post post) {
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
    }
    
}
