package me.code.individual_assignment.service;

import me.code.individual_assignment.exceptions.UserNotFoundException;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.User;
import me.code.individual_assignment.repository.FolderRepository;
import me.code.individual_assignment.repository.UserRepository;
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

    public Folder createNewFolder(int userId, String name) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Användaren kunde inte hittas"));
        Folder folder = new Folder();
        folder.setName(name);
        folder.setUser(user);
        folderRepository.save(folder);
        return folder;
    }
}
