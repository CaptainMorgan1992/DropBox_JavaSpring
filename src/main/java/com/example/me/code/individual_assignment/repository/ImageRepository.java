package com.example.me.code.individual_assignment.repository;

import com.example.me.code.individual_assignment.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository <Image, Integer> {

    boolean existsById(int id);

    String deleteById(int id);

    @Query("SELECT COUNT(i) > 0 FROM Image i " +
            "JOIN i.folder f " +
            "WHERE i.id = :imageId AND f.user.id = :userId")
    boolean existsByFolderUserIdAndImageId(int userId, int imageId);
}
