package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.dto.PostBody;
import dev.artep.latroc_backend_simple.dto.PostResponse;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Post;
import dev.artep.latroc_backend_simple.service.JWTService;
import dev.artep.latroc_backend_simple.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostControllerTest {

    private MockMvc mockMvc;
    private PostService postService;
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        jwtService = mock(JWTService.class);
        PostController postController = new PostController(postService, jwtService);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void testCreatePost() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "some image".getBytes());
        Post post = new Post();
        post.setTitle("Test Title");

        when(jwtService.validateToken(anyString())).thenReturn(true);
        when(jwtService.getUsername(anyString())).thenReturn("testUser");
        when(postService.createPost(any(PostBody.class), anyString())).thenReturn(post);

        mockMvc.perform(multipart("/posts/create")
                        .file(imageFile)
                        .param("title", "Test Title")
                        .param("description", "Test Description")
                        .param("price", "100.0")
                        .param("categoryId", "1")
                        .header("Authorization", "Bearer testToken"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));

        verify(postService, times(1)).createPost(any(PostBody.class), eq("testUser"));
    }

    @Test
    void testGetAllPosts() throws Exception {
        Post post1 = new Post();
        Post post2 = new Post();
        List<Post> posts = Arrays.asList(post1, post2);

        when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void testGetPostById() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setUser(new LocalUser());
        post.getUser().setUsername("testUser");
        post.getUser().setId(1L);

        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    void testSearchPosts() throws Exception {
        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        List<PostResponse> posts = Arrays.asList(postResponse1, postResponse2);

        when(postService.searchPosts("query")).thenReturn(posts);

        mockMvc.perform(get("/posts/search")
                        .param("query", "query")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(postService, times(1)).searchPosts("query");
    }

    @Test
    void testGetPostsByCategory() throws Exception {
        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        List<PostResponse> posts = Arrays.asList(postResponse1, postResponse2);

        when(postService.getPostsByCategory(1L)).thenReturn(posts);

        mockMvc.perform(get("/posts/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(postService, times(1)).getPostsByCategory(1L);
    }

    @Test
    void testDeletePost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setUser(new LocalUser());
        post.getUser().setUsername("testUser");

        when(postService.getPostById(1L)).thenReturn(post);
        when(jwtService.getUsername(anyString())).thenReturn("testUser");

        mockMvc.perform(delete("/posts/1")
                        .header("Authorization", "Bearer testToken"))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).deletePost(1L);
    }
}
