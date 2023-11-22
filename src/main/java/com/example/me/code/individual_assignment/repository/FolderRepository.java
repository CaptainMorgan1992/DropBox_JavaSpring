package com.example.me.code.individual_assignment.repository;

import com.example.me.code.individual_assignment.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Integer> {
}
