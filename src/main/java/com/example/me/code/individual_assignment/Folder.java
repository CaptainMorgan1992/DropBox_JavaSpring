package com.example.me.code.individual_assignment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
