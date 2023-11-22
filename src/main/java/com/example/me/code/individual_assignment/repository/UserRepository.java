package com.example.me.code.individual_assignment.repository;

import com.example.me.code.individual_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
