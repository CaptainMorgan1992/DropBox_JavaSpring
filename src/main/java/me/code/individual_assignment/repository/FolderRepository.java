package me.code.individual_assignment.repository;

import me.code.individual_assignment.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on Folder entities.
 * It extends JpaRepository, providing methods for common database operations.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

    /**
     * Custom query to check if a folder exists for a given user and folder ID.
     *
     * @param userId   The ID of the user.
     * @param folderId The ID of the folder.
     * @return True if the folder exists for the user and folder ID, false otherwise.
     */
    @Query(value = "SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM " +
            "folders f WHERE f.user_id = :userId AND f.folder_id = :folderId", nativeQuery = true)
    boolean existsByUserIdAndFolderId(int userId, int folderId);

    /**
     * Custom query to find the ID of a folder by its name.
     *
     * @param name The name of the folder.
     * @return The ID of the folder, or null if not found.
     */
    @Query(value = "SELECT f.id FROM Folder f WHERE f.name = :name")
    Integer findByName(String name);
}
