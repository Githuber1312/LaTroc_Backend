package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.dto.PostBody;
import dev.artep.latroc_backend_simple.dto.PostResponse;
import dev.artep.latroc_backend_simple.model.Category;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Post;
import dev.artep.latroc_backend_simple.repository.LocalPostDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import dev.artep.latroc_backend_simple.repository.LocalCategoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    private PostService postService;
    private LocalPostDAO mockPostDAO;
    private LocalUserDAO mockUserDAO;
    private LocalCategoryDAO mockCategoryDAO;

    @BeforeEach
    void setUp() {
        mockPostDAO = mock(LocalPostDAO.class);
        mockUserDAO = mock(LocalUserDAO.class);
        mockCategoryDAO = mock(LocalCategoryDAO.class);
        postService = new PostService(mockPostDAO, mockUserDAO, mockCategoryDAO);
    }

    @Test
    void testSearchPosts() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Post 1");
        post1.setUser(new LocalUser());
        post1.getUser().setUsername("user1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Post 2");
        post2.setUser(new LocalUser());
        post2.getUser().setUsername("user2");

        when(mockPostDAO.searchPosts("query")).thenReturn(Arrays.asList(post1, post2));

        List<PostResponse> posts = postService.searchPosts("query");

        assertEquals(2, posts.size());
        verify(mockPostDAO, times(1)).searchPosts("query");
    }

    @Test
    void testCreatePost() {
        PostBody postBody = new PostBody();
        postBody.setTitle("Test Title");
        postBody.setDescription("Test Description");
        postBody.setPrice(100.0);
        postBody.setCategoryId(1L);

        LocalUser user = new LocalUser();
        user.setUsername("testUser");

        Category category = new Category();

        when(mockUserDAO.findByUsernameIgnoreCase("testUser")).thenReturn(Optional.of(user));
        when(mockCategoryDAO.findById(1L)).thenReturn(Optional.of(category));
        when(mockPostDAO.save(any(Post.class))).thenReturn(new Post());

        Post post = postService.createPost(postBody, "testUser");

        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("testUser");
        verify(mockCategoryDAO, times(1)).findById(1L);
        verify(mockPostDAO, times(1)).save(any(Post.class));
    }

    @Test
    void testGetAllPosts() {
        Post post1 = new Post();
        Post post2 = new Post();

        when(mockPostDAO.findAll()).thenReturn(Arrays.asList(post1, post2));

        List<Post> posts = postService.getAllPosts();

        assertEquals(2, posts.size());
        verify(mockPostDAO, times(1)).findAll();
    }

    @Test
    void testGetPostById() {
        Post post = new Post();
        post.setId(1L);

        when(mockPostDAO.findById(1L)).thenReturn(Optional.of(post));

        Post foundPost = postService.getPostById(1L);

        assertNotNull(foundPost);
        assertEquals(1L, foundPost.getId());
        verify(mockPostDAO, times(1)).findById(1L);
    }

    @Test
    void testGetPostsByUserId() {
        Post post1 = new Post();
        Post post2 = new Post();

        when(mockPostDAO.findByUserId(1L)).thenReturn(Arrays.asList(post1, post2));

        List<Post> posts = postService.getPostsByUserId(1L);

        assertEquals(2, posts.size());
        verify(mockPostDAO, times(1)).findByUserId(1L);
    }

    @Test
    void testGetPostsByCategory() {
        Post post1 = new Post();
        post1.setUser(new LocalUser());
        post1.getUser().setUsername("user1");

        Post post2 = new Post();
        post2.setUser(new LocalUser());
        post2.getUser().setUsername("user2");

        when(mockPostDAO.findByCategoryId(1L)).thenReturn(Arrays.asList(post1, post2));

        List<PostResponse> posts = postService.getPostsByCategory(1L);

        assertEquals(2, posts.size());
        verify(mockPostDAO, times(1)).findByCategoryId(1L);
    }

    @Test
    void testDeletePost() {
        Post post = new Post();
        post.setId(1L);

        when(mockPostDAO.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L);

        verify(mockPostDAO, times(1)).findById(1L);
        verify(mockPostDAO, times(1)).delete(post);
    }
}
