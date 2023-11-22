package com.example.me.code.individual_assignment.security;

import com.example.me.code.individual_assignment.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenHandler {

    private Key key = Keys.hmacShaKeyFor((MySecureRandom.fetchSecureRandom()).getBytes());

    public String generateToken(User user) {
        int id = user.getId();
        String username = user.getUsername();

        return Jwts.builder()
                .setSubject(Integer.toString(id))
                .claim("id", id)
                .claim("username", username)
                .signWith(key)
                .compact();
    }
}
