package me.code.individual_assignment.repository;

import me.code.individual_assignment.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository <Image, Integer> {

    boolean existsById(int id);

    String deleteById(int id);

    @Query("SELECT COUNT(i) > 0 FROM Image i " +
            "JOIN i.folder f " +
            "WHERE i.id = :imageId AND f.user.id = :userId")
    boolean existsByFolderUserIdAndImageId(int userId, int imageId);
}
