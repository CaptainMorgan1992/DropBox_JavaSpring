package com.example.me.code.individual_assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> folders = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
/*
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

 */

    public Map<String, Object> toJson() {
        Map<String, Object> resultAsJson = new LinkedHashMap<>();
        resultAsJson.put("user_id", this.id);
        resultAsJson.put("username", this.username);
        return resultAsJson;
    }
}
