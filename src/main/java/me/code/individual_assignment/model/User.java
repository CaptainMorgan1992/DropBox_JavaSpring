package me.code.individual_assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

/**
 * Entity class representing a user in the application.
 * It is mapped to the "users" table in the database.
 */
@Table(name = "users")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Folder> folders = new ArrayList<>();

    /**
     * Constructor for creating a User with a username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Converts the user entity to a JSON representation.
     *
     * @return A map representing the JSON structure of the user.
     */
    public Map<String, Object> toJson() {
        Map<String, Object> resultAsJson = new LinkedHashMap<>();
        resultAsJson.put("username", this.username);
        resultAsJson.put("user_id", this.id);
        resultAsJson.put("folders", getFoldersAsJson());
        return resultAsJson;
    }

    /**
     * Converts the user's folders to a JSON representation.
     *
     * @return A list of maps representing the JSON structure of each folder.
     */
    private List<Map<String, Object>> getFoldersAsJson() {
        List<Map<String, Object>> foldersJson = new ArrayList<>();
        for (Folder folder : this.folders) {
            foldersJson.add(folder.toJson());
        }
        return foldersJson;
    }
}