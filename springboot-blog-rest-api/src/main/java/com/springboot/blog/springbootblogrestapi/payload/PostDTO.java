package com.springboot.blog.springbootblogrestapi.payload;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String description;
    private String content;
    private Set<CommentDTO> comments;
    
}
