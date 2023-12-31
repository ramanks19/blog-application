package com.springboot.blog.springbootblogrestapi.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CommentDTO {

    private Long id;

    @Size(min = 1, message = "Name cannot be blank")
    private String name;

    @Size(min = 1, message = "Email cannot be blank")
    @Email(message = "Proper email should be provided")
    private String email;

    @Size(min = 1, message = "Body cannot be blank")
    private String body;
}
