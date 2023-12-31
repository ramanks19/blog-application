package com.springboot.blog.springbootblogrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String name;
    private String userName;
    private String email;
    private String password;

    private String role;
    
}
