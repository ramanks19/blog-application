package com.springboot.blog.springbootblogrestapi.payload;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import com.springboot.blog.springbootblogrestapi.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    
    private Long id;

    @Size(min = 1, message = "Name cannot be blank")
    private String name;

    @Size(min = 1, message = "Description cannot be blank")
    private String description;

//    private Set<Post> post;
    private List<PostDTO> posts;
}
