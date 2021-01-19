package com.example.connection.repository;

import com.example.connection.model.Comments;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepo extends CrudRepository<Comments, Integer> {
    @Query(value = "Select max(id) from comments", nativeQuery = true)
    Integer findMinimum();
    Iterable<Comments> findAllByPostOrderByCommentdateDesc(Integer post);
}
