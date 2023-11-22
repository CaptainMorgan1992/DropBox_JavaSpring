package com.example.me.code.individual_assignment.service;

import com.example.me.code.individual_assignment.exceptions.UserNotFoundException;
import com.example.me.code.individual_assignment.model.Folder;
import com.example.me.code.individual_assignment.model.User;
import com.example.me.code.individual_assignment.repository.FolderRepository;
import com.example.me.code.individual_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    private FolderRepository folderRepository;
    private UserRepository userRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public Folder createNewFolder(int userId, String name){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Användaren kunde inte hittas"));
        Folder folder = new Folder();
        folder.setName(name);
        folder.setUser(user);
        folderRepository.save(folder);
        return folder;
    }
}
