package com.example.me.code.individual_assignment.repository;

import com.example.me.code.individual_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM users u " +
            "WHERE u.username = :username AND u.password = :password", nativeQuery = true)
    boolean isValidUser(String username, String password);

   User findByUsername(String username);


    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.folders " +
            "WHERE u.id = :userId")
    Optional<User> getUserInfo(@Param("userId") int userId);


    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM users u " +
            "WHERE u.username = :username AND u.user_id = :userId", nativeQuery = true)
    boolean compareUsernameWithId(String username, int userId);
}
