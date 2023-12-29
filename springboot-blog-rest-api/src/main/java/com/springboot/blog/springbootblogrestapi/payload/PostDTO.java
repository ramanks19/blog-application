package com.springboot.blog.springbootblogrestapi.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostDTO {

    private Long id;

    @NotNull
    @Size(min = 2, message = "Post title should have atleast 2 characters")
    private String title;
    
    @NotNull
    @Size(min = 10, message = "Post description should have atleast 10 characters")
    private String description;

    @NotNull
    private String content;
    
}
