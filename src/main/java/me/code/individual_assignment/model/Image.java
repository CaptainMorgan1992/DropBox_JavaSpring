package me.code.individual_assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Entity class representing an image in the application.
 * It is mapped to the "images" table in the database.
 */
@Entity
@Getter
@Setter
@Table(name = "images")
@NoArgsConstructor
public class Image {

    @Id
    @Column(name = "image_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "size")
    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    /**
     * Constructor for creating an Image from a MultipartFile and associating it with a folder.
     *
     * @param file   The MultipartFile representing the image file.
     * @param folder The folder to which the image belongs.
     * @throws IOException If an I/O exception occurs during file processing.
     */
    public Image(MultipartFile file, Folder folder) throws IOException {
        this.name = file.getOriginalFilename();
        this.data = file.getBytes();
        this.size = file.getSize();
        this.folder = folder;
    }

    /**
     * Converts the image entity to a JSON representation.
     *
     * @return A map representing the JSON structure of the image.
     */
    public Map<String, Object> toJson() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("image_id", this.id);
        result.put("filename", this.name);
        return result;
    }
}