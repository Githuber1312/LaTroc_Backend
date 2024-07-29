package dev.artep.latroc_backend_simple.repository;

import dev.artep.latroc_backend_simple.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocalPostDAO extends CrudRepository<Post, Long> {
    List<Post> findByUserId(long userId);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %?1% OR p.description LIKE %?1%")
    List<Post> searchPosts(String query);

    List<Post> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String keyword1);

    List<Post> findByCategoryId(Long categoryId);
}
