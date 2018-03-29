package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseUser;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import java.util.Base64;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String createUser(@RequestBody DatabaseUser databaseUser){
        User user = new User(databaseUser.getUsername(), databaseUser.getPassword());
        userRepository.save(user);
        String authorization = databaseUser.getUsername() + ":" + databaseUser.getPassword();
        return new String(Base64.getEncoder().encode(authorization.getBytes()));
    }
}