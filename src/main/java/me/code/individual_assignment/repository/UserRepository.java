package me.code.individual_assignment.repository;

import me.code.individual_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Checks if a user with the specified username and password exists.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return {@code true} if a user with the specified username and password exists, {@code false} otherwise.
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM users u " +
            "WHERE u.username = :username AND u.password = :password", nativeQuery = true)
    boolean isValidUser(String username, String password);

    /**
     * Finds a user by the username.
     *
     * @param username The username of the user.
     * @return The user with the specified username, or {@code null} if not found.
     */
    User findByUsername(String username);

    /**
     * Gets user information including associated folders by user ID.
     *
     * @param userId The ID of the user.
     * @return An Optional containing the user information, or an empty Optional if not found.
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.folders " +
            "WHERE u.id = :userId")
    Optional<User> getUserInfo(@Param("userId") int userId);

    /**
     * Compares a username with a user ID to check if it exists.
     *
     * @param username The username of the user.
     * @param userId   The ID of the user.
     * @return {@code true} if a user with the specified username and user ID exists, {@code false} otherwise.
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM users u " +
            "WHERE u.username = :username AND u.user_id = :userId", nativeQuery = true)
    boolean compareUsernameWithId(String username, int userId);

}
