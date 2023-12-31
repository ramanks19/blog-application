package com.springboot.blog.springbootblogrestapi.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComplexPassword {
    String message() default "Invalid Password";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}
