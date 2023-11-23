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

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;


    public Image(MultipartFile file, Folder folder) throws IOException {
        this.name = file.getOriginalFilename();
        this.data = file.getBytes();
        this.size = file.getSize();
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data=" + Arrays.toString(data) +
                ", size=" + size +
                ", folder=" + folder +
                '}';
    }
}
