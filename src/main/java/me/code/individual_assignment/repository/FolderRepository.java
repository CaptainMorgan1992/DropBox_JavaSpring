package me.code.individual_assignment.repository;

import me.code.individual_assignment.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM " +
            "folders f WHERE f.user_id = :userId AND f.folder_id = :folderId",
            nativeQuery = true)
    boolean existsByUserIdAndFolderId(int userId, int folderId);

    @Query(value = "SELECT f FROM Folder f WHERE f.id = :folderId")
    Optional<Folder> findByFolderId(int folderId);

    @Query(value = "SELECT f.id FROM Folder f WHERE f.name = :name")
    Integer findByName(String name);
}
