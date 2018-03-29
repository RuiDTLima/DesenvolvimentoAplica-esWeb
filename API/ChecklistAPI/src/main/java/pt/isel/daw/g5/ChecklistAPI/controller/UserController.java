package pt.isel.daw.g5.ChecklistAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g5.ChecklistAPI.exceptions.RepeatedInformationException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseUser;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(ChecklistController.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity createUser(@RequestBody DatabaseUser databaseUser){
        log.info("User is being created");
        if (userRepository.existsById(databaseUser.getUsername())){
            log.warn("A User with that username already exists.");
            InvalidParams repeatedUser = new InvalidParams("username", "username already exists");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid credentials.", 409, "The username provided already exists.", "/users", new InvalidParams[]{repeatedUser});
            throw new RepeatedInformationException(problemJSON);
        }

        User user = new User(databaseUser.getUsername(), databaseUser.getPassword());
        userRepository.save(user);
        String authorization = databaseUser.getUsername() + ":" + databaseUser.getPassword();
        log.info("User successfully created");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}