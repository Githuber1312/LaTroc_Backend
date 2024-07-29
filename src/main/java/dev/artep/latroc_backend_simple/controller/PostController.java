package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.dto.PostBody;
import dev.artep.latroc_backend_simple.dto.PostResponse;
import dev.artep.latroc_backend_simple.model.Post;
import dev.artep.latroc_backend_simple.service.JWTService;
import dev.artep.latroc_backend_simple.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private PostService postService;
    private JWTService jwtService;

    private final String UPLOAD_DIR = "uploads/";

    public PostController(PostService postService, JWTService jwtService) {
        this.postService = postService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String token,
                                           @RequestParam("title") String title,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") Double price,
                                           @RequestParam("categoryId") Long categoryId,
                                           @RequestParam("image") MultipartFile image) throws IOException {

        String parsedToken = token.replace("Bearer ", "").split(";")[0];

        if (!jwtService.validateToken(parsedToken)) {
            throw new RuntimeException("Invalid token");
        }
        String username = jwtService.getUsername(parsedToken);

        // Save the image to the server
        String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(UPLOAD_DIR + imageName);
        Files.createDirectories(imagePath.getParent());
        Files.copy(image.getInputStream(), imagePath);

        // Create the post with the image URL
        PostBody postBody = new PostBody();
        postBody.setTitle(title);
        postBody.setDescription(description);
        postBody.setPrice(price);
        postBody.setCategoryId(categoryId);
        postBody.setImageUrl(imagePath.toString());

        Post createdPost = postService.createPost(postBody, username);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        String username = post.getUser().getUsername();
        Long userId = post.getUser().getId();
        PostResponse response = new PostResponse(post, username, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam("query") String query) {
        List<PostResponse> posts = postService.searchPosts(query);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostResponse>> getPostsByCategory(@PathVariable Long categoryId) {
        List<PostResponse> posts = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        String username = jwtService.getUsername(token.replace("Bearer ", ""));
        Post post = postService.getPostById(id);

        if (!post.getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
