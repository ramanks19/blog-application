package com.springboot.blog.springbootblogrestapi.entity;

import java.util.Set;

//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.Size;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import com.springboot.blog.springbootblogrestapi.utils.ComplexPassword;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    @Size(min = 1, message = "Name cannot be empty")
    @Column(name = "NAME", nullable = false)
    private String name;

    @Size(min = 5, message = "Username cannot be less than 5 characters")
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String userName;

    @Email(message = "Provide a valid email")
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @ComplexPassword(message = "Password must be at least 5 characters and include a mix of uppercase, lowercase, symbols, and digits")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ROLE_ID")
    )
    private Set<Role> roles;
}
