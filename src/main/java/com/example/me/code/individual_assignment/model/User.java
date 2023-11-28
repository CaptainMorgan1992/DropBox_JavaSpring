package com.example.me.code.individual_assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Table(name="users")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {

    @Column(name = "user_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY,  orphanRemoval = true)
    private Set<Folder> folders = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> resultAsJson = new LinkedHashMap<>();
        resultAsJson.put("user_id", this.id);
        resultAsJson.put("username", this.username);
        resultAsJson.put("folders", getFoldersAsJson());
        return resultAsJson;
    }

    private List<Map<String, Object>> getFoldersAsJson() {
        List<Map<String, Object>> foldersJson = new ArrayList<>();
        for (Folder folder : this.folders) {
            foldersJson.add(folder.toJson());
        }
        return foldersJson;
    }
}
