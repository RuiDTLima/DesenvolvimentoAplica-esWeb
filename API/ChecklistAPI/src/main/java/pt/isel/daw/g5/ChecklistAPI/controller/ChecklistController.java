package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.exceptions.InvalidStateException;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.Checklists;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import java.util.Optional;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @GetMapping
    public Checklists getChecklists(@RequestParam(value = "page", defaultValue = "1") int page){
        /*Page<Checklist> checklistPage = checklistRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE));
        return new Checklists(checklistPage);*/
        throw new InvalidStateException("invalid state");
    }

    @PostMapping
    public String postChecklist(@RequestBody Checklist checklist){
        Optional<User> rui = userRepository.findById("Rui");
        checklist.setUsername(rui.get()); //TODO verificar autenticação
        checklistRepository.save(checklist);
        return "Ok";
    }

    @GetMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public OutChecklistItem getChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                             @PathVariable("checklistitem_id") int checklistitem_id){

        Optional<ChecklistItem> checklistItem = checklistItemRepository.findById(checklistitem_id);
        OutChecklistItem outChecklistItem = new OutChecklistItem(checklistItem);
        return outChecklistItem;
    }

    @DeleteMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public String deleteChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                      @PathVariable("checklistitem_id") int checklistitem_id){

        checklistItemRepository.deleteById(checklistitem_id);
        return "OK";
    }

    @PutMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public String putChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                   @PathVariable("checklistitem_id") int checklistitem_id, @RequestBody ChecklistItem checklistItem){

        if (!checklistItemRepository.existsById(checklistitem_id))
            return "Não existe";    //TODO enviar erro de id
        checklistItemRepository.save(checklistItem);
        return "OK";
    }
}