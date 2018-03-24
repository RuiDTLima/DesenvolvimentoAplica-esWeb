package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.Checklists;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    ChecklistRepository checklistRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public Checklists getChecklists(@RequestParam(value = "page", defaultValue = "1") int page){
        Page<Checklist> checklistPage = checklistRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE));
        Checklists checklists = new Checklists(checklistPage);
        return checklists;
    }

    @PostMapping
    public String postChecklist(@RequestBody Checklist checklist){
        Optional<User> rui = userRepository.findById("Rui");
        checklist.setUsername(rui.get());
        checklistRepository.save(checklist);
        return "Ok";
    }
}
