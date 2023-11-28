package com.example.me.code.individual_assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name="folders")
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
    @JsonIgnore // Exclude user from JSON serialization
    private User user;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List <Image> images = new ArrayList<>();

    public Map<String, Object> toJson() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("folder_id", this.id);
        result.put("user", this.user.toJson());
        result.put("images", getImagesAsJson());
        return result;
    }

    private List<Map<String, Object>> getImagesAsJson() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Image image : this.images) {
            result.add(image.toJson());
        }
        return result;
    }

}
