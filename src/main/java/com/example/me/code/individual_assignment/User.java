package com.example.me.code.individual_assignment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name="users")
@Getter
@Setter
@Entity
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> folders = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
