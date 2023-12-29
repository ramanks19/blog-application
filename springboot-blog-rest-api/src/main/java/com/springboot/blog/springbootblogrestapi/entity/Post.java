package com.springboot.blog.springbootblogrestapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "POSTS", 
       uniqueConstraints = {@UniqueConstraint(columnNames = {"TITLE"})})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "TITLE", nullable = false)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotBlank(message = "Content cannot be blank")
    @Size(min = 20, message = "Content should atleast be 20 characters")
    @Column(name = "CONTENT", nullable = false)
    private String content;
    
}
