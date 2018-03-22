package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g5.ChecklistAPI.model.Data;
import pt.isel.daw.g5.ChecklistAPI.model.Items;
import pt.isel.daw.g5.ChecklistAPI.model.Link;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.Checklists;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import java.util.Arrays;

@RestController
public class WebController {
    @Autowired
    UserRepository repository;

    @RequestMapping("/save")
    public String save(){
        // save a single Customer
        repository.save(new User("Jack", "Smith"));

        // save a list of Customers
        repository.saveAll(Arrays.asList(new User("Adam", "Johnson"), new User("Kim", "Smith"),
                new User("David", "Williams"), new User("Peter", "Davis")));

        return "Done";
    }

    @GetMapping("/findAll")
    public Checklists findAll(){
        /*String result = "";

        for (User user : repository.findAll()) {
            result += user.toString() + "<br>";
        }
        return result;*/
        Checklists checklists = new Checklists("1.0", "/checklists?page=1", new Items[]{new Items("/checklists/1", new Data[]{new Data("name", "Lista 1")}, new Link[]{new Link("items", "/checklists/1/checklistitems")})}, new Link[]{new Link("next", "/checklists?page=2")});
        return checklists;
    }
}
