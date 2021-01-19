package com.example.connection.repository;

import com.example.connection.model.Subscribes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribesRepo extends JpaRepository<Subscribes, Integer> {
        Iterable<Subscribes> findTop5ByWho(String who);
        Iterable<Subscribes>findAllByWho(String who);
        Iterable<Subscribes>findAllByWhom(String whom);
        void deleteByWhomAndWho(@Param("whom")String whom, @Param("who") String who);
        @Query(value = "Select count(id) from subscribes where who = ?1", nativeQuery = true)
        Integer findCountWhom1(@Param("who")String name,@Param("whom")String sub);

        @Query(value = "Select count(id) from subscribes where whom = ?1", nativeQuery = true)
        Integer findCountWho(String name);
        @Query(value = "Select count(id) from subscribes where who = ?1", nativeQuery = true)
        Integer findCountWhom(String name);

        @Query(value = "Select max(id) from subscribes", nativeQuery = true)
        Integer findMax();


}