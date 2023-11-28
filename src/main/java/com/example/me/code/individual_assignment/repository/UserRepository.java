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

   Optional<User> findByUsername(String username);


/*
    @Query("SELECT DISTINCT ff FROM Folder ff " +
            "JOIN FETCH ff.user u " +
            "LEFT JOIN FETCH ff.images i " +
            "WHERE u.id = :userId")
    List<User> getUserInfo(int userId);

 */

    /*
    @Query(value = "")
    Optional<User> findByUserId(int userId);

     */

   /*
    @Query("SELECT u.id as user_id, f.id as folder_id, i.id as image_id " +
            "FROM User u " +
            "LEFT JOIN Folder f ON u.id = f.user.id " +
            "LEFT JOIN Image i ON f.id = i.folder.id " +
            "WHERE u.username = :username")
    List<Object[]> findUserInfoByUsername(@Param("username") String username);

    */
}
