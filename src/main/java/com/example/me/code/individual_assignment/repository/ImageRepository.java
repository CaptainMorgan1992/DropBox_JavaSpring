package com.example.me.code.individual_assignment.repository;

import com.example.me.code.individual_assignment.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository <Image, Integer> {
}
