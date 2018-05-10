package pt.isel.daw.g5.ChecklistAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotFoundException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.RepeatedInformationException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseUser;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutUser;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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
            ProblemJSON problemJSON = new ProblemJSON("/invalid-username-error", "Invalid credentials.", 409, "The username provided already exists.", "/users", new InvalidParams[]{repeatedUser});
            throw new RepeatedInformationException(problemJSON);
        }

        User user = new User(databaseUser.getUsername(), databaseUser.getPassword());
        userRepository.save(user);
        log.info("User successfully created");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{username}",produces = "application/vnd.siren+json")
    public OutUser getUser(@PathVariable("username") String username, HttpServletResponse response) {
        Optional<User> user =  userRepository.findById(username);
        if(!user.isPresent()) {
            String msg = "The user doesn't exit.";
            log.warn(msg);
            throw new NotFoundException(msg);
        }
        return new OutUser(new DatabaseUser(user.get()));
    }
}