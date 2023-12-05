package me.code.individual_assignment.service;

import me.code.individual_assignment.exceptions.UserNotFoundException;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.User;
import me.code.individual_assignment.repository.FolderRepository;
import me.code.individual_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The FolderService class provides services related to folder operations.
 */
@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for the FolderService class.
     *
     * @param folderRepository The repository for folder data.
     * @param userRepository   The repository for user data.
     */
    @Autowired
    public FolderService(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new folder for the specified user with the given name.
     *
     * @param userId The ID of the user for whom the folder is created.
     * @param name   The name of the new folder.
     * @return The created folder.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    public Folder createNewFolder(int userId, String name) throws UserNotFoundException {
        // Retrieve the user from the repository based on the provided user ID.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Användaren kunde inte hittas"));

        // Create a new folder and set its name and associated user.
        Folder folder = new Folder();
        folder.setName(name);
        folder.setUser(user);

        // Save the folder to the repository.
        folderRepository.save(folder);

        // Return the created folder.
        return folder;
    }
}
