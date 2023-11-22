package com.example.me.code.individual_assignment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="images")
public class Image {

    @Id
    @Column(name = "image_id")
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
}
