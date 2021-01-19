package com.example.connection.repository;

import com.example.connection.model.Posts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepo  extends CrudRepository<Posts, Integer> {
    Iterable<Posts> findAllByAuthorOrderByPostdateDesc(String author);
    void deleteByAuthorAndPostdate(@Param("author")String author,@Param("postdate") String date);
    Iterable<Posts> findAllById(Integer id);

    @Query(value = "Select max(id) from posts", nativeQuery = true)
    Integer findMax();

    @Query(value = "Select count(id) from posts where author = ?1", nativeQuery = true)
    Integer findCount(String login);

    Iterable<Posts>  findTop10ByHeaderIsContaining(String header);

}
