package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.Home;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping//@GetMapping(produces = "application/json-home") //TODO porque não aceita o formato da resposta?
    public Home getHome(){
        return new Home();
    }
}