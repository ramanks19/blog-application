package com.springboot.blog.springbootblogrestapi;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Kuch Alfaaz",
				description = "REST APIs Documentation for Kuch Alfaaz",
				version = "v1.0",
				contact = @Contact(
						name = "Raman Kumar Singh",
						email = "singhramankumar1993@gmail.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Link to the code source for the Blog",
				url = "https://github.com/ramanks19/blog-application"
		)
)
public class SpringbootBlogRestApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

}
