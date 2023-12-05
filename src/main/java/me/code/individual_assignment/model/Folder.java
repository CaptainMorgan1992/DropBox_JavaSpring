package me.code.individual_assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

/**
 * Entity class representing a folder in the application.
 * It is mapped to the "folders" table in the database.
 */
@Entity
@Getter
@Setter
@Table(name = "folders")
@NoArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id", nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    /**
     * Converts the folder entity to a JSON representation.
     * @return A map representing the JSON structure of the folder.
     */
    public Map<String, Object> toJson() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("folder_id", this.id);
        result.put("user", this.user.toJson());
        result.put("images", getImagesAsJson());
        return result;
    }

    /**
     * Converts the list of images associated with the folder to a JSON representation.
     * @return A list of maps representing the JSON structure of each image.
     */
    private List<Map<String, Object>> getImagesAsJson() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Image image : this.images) {
            result.add(image.toJson());
        }
        return result;
    }
}