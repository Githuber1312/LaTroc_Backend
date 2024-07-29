package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.dto.PostBody;
import dev.artep.latroc_backend_simple.dto.PostResponse;
import dev.artep.latroc_backend_simple.exception.PostNotFoundException;
import dev.artep.latroc_backend_simple.model.Category;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Post;
import dev.artep.latroc_backend_simple.repository.LocalPostDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import dev.artep.latroc_backend_simple.repository.LocalCategoryDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private LocalPostDAO localPostDAO;
    private LocalUserDAO localUserDAO;
    private LocalCategoryDAO localCategoryDAO;

    public List<PostResponse> searchPosts(String query) {
        return localPostDAO.searchPosts(query).stream().map(post ->
                new PostResponse(post, post.getUser().getUsername(), post.getUser().getId())
        ).collect(Collectors.toList());
    }

    public PostService(LocalPostDAO localPostDAO, LocalUserDAO localUserDAO, LocalCategoryDAO localCategoryDAO) {
        this.localPostDAO = localPostDAO;
        this.localUserDAO = localUserDAO;
        this.localCategoryDAO = localCategoryDAO;
    }

    public Post createPost(PostBody postBody, String username) {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase(username).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = localCategoryDAO.findById(postBody.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Post post = new Post();
        post.setTitle(postBody.getTitle());
        post.setDescription(postBody.getDescription());
        post.setPrice(postBody.getPrice());
        post.setImageUrl(postBody.getImageUrl());
        post.setTimestamp(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);
        return localPostDAO.save(post);
    }

    public List<Post> getAllPosts() {
        return (List<Post>) localPostDAO.findAll();
    }

    public Post getPostById(Long id) {
        return localPostDAO.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public List<Post> getPostsByUserId(Long id){
        return localPostDAO.findByUserId(id);
    }

    public List<PostResponse> getPostsByCategory(Long categoryId) {
        List<Post> posts = localPostDAO.findByCategoryId(categoryId);
        return posts.stream()
                .map(post -> new PostResponse(post, post.getUser().getUsername(), post.getUser().getId()))
                .collect(Collectors.toList());
    }

    public void deletePost(Long id) {
        Post post = localPostDAO.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        post.setUser(null);
        localPostDAO.delete(post);
    }

}
