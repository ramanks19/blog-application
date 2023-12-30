package com.springboot.blog.springbootblogrestapi.payload;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String name;
    private String email;
    private String body;
}
