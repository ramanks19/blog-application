package com.springboot.blog.springbootblogrestapi.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.springboot.blog.springbootblogrestapi.entity.Category;
import com.springboot.blog.springbootblogrestapi.entity.Post;
import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.payload.PostDTO;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;
import com.springboot.blog.springbootblogrestapi.repository.CategoryRepository;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import com.springboot.blog.springbootblogrestapi.service.PostService;

@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDTO createPost(String categoryName, PostDTO postDTO) {
        Post post = mapToEntity(postDTO);
        Category category = checkValidCategory(categoryName);
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        return mapToDTO(newPost);
    }

    @Override
    public List<PostDTO> getAllPostsByCategoryName(String categoryName) {
        Category category = checkValidCategory(categoryName);
        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostById(String categoryName, Long postId) {
        Category category = checkValidCategory(categoryName);
        Post post = getPostByIdOrThow(postId);
        validatePostBelongsToCategory(post, category);
        
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(String categoryName, Long postId, PostDTO postDTO, Set<String> fieldsToUpdate) {
        Category category = checkValidCategory(categoryName);
        Post post = getPostByIdOrThow(postId);
        validatePostBelongsToCategory(post, category);

        Set<String> validFields = Arrays.stream(Post.class.getDeclaredFields())
                                        .map(Field::getName)
                                        .collect(Collectors.toSet());
        
        if (!validFields.containsAll(fieldsToUpdate)) {
            Set<String> invalidFields = new HashSet<>(fieldsToUpdate);
            invalidFields.removeAll(validFields);
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid fields are: " + invalidFields);
        }

        if (fieldsToUpdate.contains("title")) {
             post.setTitle(postDTO.getTitle());
         }
         if (fieldsToUpdate.contains("description")) {
             post.setDescription(postDTO.getDescription());
         }
         if (fieldsToUpdate.contains("content")) {
             post.setContent(postDTO.getContent());
         }

         Post updatedPost = postRepository.save(post);
         return mapToDTO(updatedPost);

    }

    @Override
    public void deletePost(String categoryName, Long postId) {
        Category category = checkValidCategory(categoryName);
        Post post = getPostByIdOrThow(postId);
        validatePostBelongsToCategory(post, category);

        postRepository.delete(post);
    }

    //convert entity to DTO
    private PostDTO mapToDTO(Post post) {
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        return postDTO;
    }

    //convert dto to entity
    private Post mapToEntity(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        return post;
    }

    //check whether it is a valid category
    private Category checkValidCategory(String categoryName) {
        return categoryRepository.findByName(categoryName).orElseThrow(
            () -> new ResourceNotFoundException("Category", categoryName)
        );
    }

    //check whether it is a valid post
    private Post getPostByIdOrThow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new ResourceNotFoundException("Post", "postId", postId)
        );
    }

    //check whether a post belongs to a category
    private void validatePostBelongsToCategory(Post post, Category category) {
        if (!post.getCategory().getId().equals(category.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Post does not belong to category");
        }
    }



    
//     @Override
//     public PostDTO createPostDTO(PostDTO postDTO) {
//         //Convert DTO to entity
//         // Post post = new Post();
//         // post.setTitle(postDTO.getTitle());
//         // post.setDescription(postDTO.getDescription());
//         // post.setContent(postDTO.getContent());

//         //Convert entity to DTO
//         // PostDTO postResponse = new PostDTO();
//         // postResponse.setId(newPost.getId());
//         // postResponse.setTitle(newPost.getTitle());
//         // postResponse.setDescription(newPost.getDescription());
//         // postResponse.setContent(newPost.getContent());
        
//         Post post = mapToEntity(postDTO);
//         Post newPost = postRepository.save(post);
//         PostDTO postResponse = mapToDTO(newPost);

//         return postResponse;
//     }

//     @Override
// //    public List<PostDTO> getAllPosts(int pageNo, int pageSize) {      
//         // List<Post> posts = postRepository.findAll();
//         // return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        
//         //create Pageable instance
//     public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
//         Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
//                     : Sort.by(sortBy).descending();

//         Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//         Page<Post> posts = postRepository.findAll(pageable);
//         //get content for page object
//         List<Post> listOfPosts = posts.getContent();
//         List<PostDTO> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

//         PostResponse postResponse = new PostResponse();
//         postResponse.setContent(content);
//         postResponse.setPageNo(posts.getNumber());
//         postResponse.setPageSize(posts.getSize());
//         postResponse.setTotalElements(posts.getTotalElements());
//         postResponse.setTotalPages(posts.getTotalPages());
//         postResponse.setLast(posts.isLast());

//         return postResponse;
//     }

//     @Override
//     public PostDTO getPostById(long id) {
//         Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
//         return mapToDTO(post);
//     }

//     @Override
//     public PostDTO updatePost(PostDTO postDTO, long id, Set<String> fieldsToUpdate) {
//         Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

//         // post.setTitle(postDTO.getTitle());
//         // post.setDescription(postDTO.getDescription());
//         // post.setContent(postDTO.getContent());
//         //Update only the specified fields
//         if (fieldsToUpdate.contains("title")) {
//             post.setTitle(postDTO.getTitle());
//         }
//         if (fieldsToUpdate.contains("description")) {
//             post.setDescription(postDTO.getDescription());
//         }
//         if (fieldsToUpdate.contains("content")) {
//             post.setContent(postDTO.getContent());
//         }

//         Post updatedPost = postRepository.save(post);
//         return mapToDTO(updatedPost);
//     }

//     @Override
//     public void deletePostById(long id) {
//         Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
//         postRepository.delete(post);
//     }

//     //Convert DTO to entity
//     private Post mapToEntity(PostDTO postDTO) {
//         // Post post = new Post();
//         // post.setTitle(postDTO.getTitle());
//         // post.setDescription(postDTO.getDescription());
//         // post.setContent(postDTO.getContent());
//         Post post = modelMapper.map(postDTO, Post.class);

//         return post;
//     }

//     //Convert entity to DTO
//     private PostDTO mapToDTO(Post post) {
//         // PostDTO postDTO = new PostDTO();
//         // postDTO.setId(post.getId());
//         // postDTO.setTitle(post.getTitle());
//         // postDTO.setDescription(post.getDescription());
//         // postDTO.setContent(post.getContent());
//         PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        
//         return postDTO;
//     }
    
}
