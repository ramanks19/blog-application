package com.springboot.blog.springbootblogrestapi.payload;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostDTO {

    private Long id;

    @Size(min = 1, message = "Title cannot be blank")
    private String title;
    
    @Size(min = 1, message = "Description cannot be blank")
    private String description;
    
    @Size(min = 10, message = "Content should atleast be 10 characters")
    private String content;

    private Set<CommentDTO> comments;
    
}
