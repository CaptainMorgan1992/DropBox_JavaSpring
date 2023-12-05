package me.code.individual_assignment.repository;

import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository <Image, Integer> {

    boolean existsById(int id);

    String deleteById(int id);

    @Query("SELECT COUNT(i) > 0 FROM Image i " +
            "JOIN i.folder f " +
            "WHERE i.id = :imageId AND f.user.id = :userId")
    boolean existsByFolderUserIdAndImageId(int userId, int imageId);

    @Query("SELECT i.id FROM Image i WHERE i.name = :imageName")
    Integer findImageIdByName(@Param("imageName") String imageName);
}
