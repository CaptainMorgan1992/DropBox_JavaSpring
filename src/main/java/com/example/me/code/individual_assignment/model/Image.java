package com.example.me.code.individual_assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name="images")
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


    public Image(MultipartFile file, Folder folder) throws IOException {
        this.name = file.getOriginalFilename();
        this.data = file.getBytes();
        this.size = file.getSize();
        this.folder = folder;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("image_id", this.id);
        result.put("filename", this.name);
        return result;
    }
}
