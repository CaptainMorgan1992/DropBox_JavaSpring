package me.code.individual_assignment.repository;

import me.code.individual_assignment.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Image entities.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    /**
     * Check if an image with the specified ID exists.
     *
     * @param id The ID of the image.
     * @return true if an image with the specified ID exists, false otherwise.
     */
    boolean existsById(int id);

    /**
     * Check if an image with the specified ID and associated folder's user ID exists.
     *
     * @param userId  The user ID of the folder owner.
     * @param imageId The ID of the image.
     * @return true if an image with the specified ID and associated folder's user ID exists, false otherwise.
     */
    @Query("SELECT COUNT(i) > 0 FROM Image i " +
            "JOIN i.folder f " +
            "WHERE i.id = :imageId AND f.user.id = :userId")
    boolean existsByFolderUserIdAndImageId(int userId, int imageId);

    /**
     * Find the ID of an image by its name.
     *
     * @param imageName The name of the image.
     * @return The ID of the image with the specified name, or null if not found.
     */
    @Query("SELECT i.id FROM Image i WHERE i.name = :imageName")
    Integer findImageIdByName(@Param("imageName") String imageName);
}
