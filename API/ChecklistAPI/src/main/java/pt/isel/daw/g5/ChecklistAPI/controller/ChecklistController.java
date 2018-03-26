package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.RequiresAuthentication;
import pt.isel.daw.g5.ChecklistAPI.exceptions.InvalidStateException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.ChecklistItems;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklists;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import java.util.Optional;

@RestController
@RequestMapping("/checklists")
@RequiresAuthentication
public class ChecklistController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;

    @GetMapping
    public OutChecklists getChecklists(@RequestParam(value = "page", defaultValue = "0") int page){
        Page<Checklist> checklistPage = checklistRepository.findAll(PageRequest.of(page, PAGE_SIZE));
        return new OutChecklists(checklistPage);
        //throw new InvalidStateException("invalid state");
    }

    @PostMapping
    public String postChecklist(@RequestBody DatabaseChecklist databaseChecklist) throws Exception {
        Optional<User> rui = userRepository.findById("Rui");
        Checklist checklist = new Checklist(databaseChecklist.getName(), databaseChecklist.getCompletionDate());

        if (databaseChecklist.getChecklisttemplate_id() != 0){
            Optional<ChecklistTemplate> checklistTemplate = checklistTemplateRepository.findById(databaseChecklist.getChecklisttemplate_id());
            if (!checklistTemplate.isPresent())
                throw new Exception();
            checklist.setChecklistTemplate(checklistTemplate.get());
        }

        checklist.setUsername(rui.get()); //TODO verificar autenticação
        checklistRepository.save(checklist);
        return "Ok";
    }

    @GetMapping("/{checklist_id}")
    public OutChecklist getChecklist(@PathVariable("checklist_id") int checklistId){
        Optional<Checklist> checklistOptional = checklistRepository.findById(checklistId);
        Checklist checklist = checklistOptional.get();
        return new OutChecklist(checklist);
    }

    @GetMapping("/{checklist_id}/checklistitems")
    public ChecklistItems getChecklistItems(@PathVariable("checklist_id") int checklistId) {
        if(!checklistRepository.existsById(checklistId)) return null;
//            throw new ChangeSetPersister.NotFoundException();
//        Page<OutChecklistItem> checklistItemPage = checklistRepository.findById(checklistId).get().getInChecklistItems();
        return null; //TODO paging
    }

    @PostMapping("/{checklist_id}/checklistitems")
    public String addChecklistItem(
            @PathVariable("checklist_id") int checklistId,
            @RequestBody ChecklistItem checklistItem
    ){
        if(!checklistRepository.existsById(checklistId))
            return "ERROR"; // TODO
        checklistItemRepository.save(checklistItem);
        return "OK";
    }

    @DeleteMapping("/{checklist_id}")
    public String deleteChecklist(@PathVariable("checklist_id") int checklistId){
        checklistRepository.deleteById(checklistId);
        return "OK";
    }

    @PutMapping("/{checklist_id}")
    public String updateChecklist(
            @PathVariable("checklist_id") int checklistId,
            @RequestBody Checklist checklist){
        if(!checklistRepository.existsById(checklistId))
            return "ERROR"; // TODO
        checklistRepository.save(checklist);
        return "OK";
    }

    @GetMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public OutChecklistItem getChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                             @PathVariable("checklistitem_id") int checklistitem_id){

        /*Optional<Checklist> checklist = checklistRepository.findById(checklist_id);
        for (ChecklistItem nextItem : checklist.get().getInChecklistItems()) {
            if (nextItem.getId() == checklistitem_id) {
                OutChecklistItem outChecklistItem = new OutChecklistItem(nextItem);
                return outChecklistItem;
            }
        }
        return null;*/
        InvalidParams invalidParams = new InvalidParams("state", "state must be either 'completed' or 'uncompleted", "finished");
        ProblemJSON problemJSON = new ProblemJSON("/validation-error", "Your request parameters didn't validate.", 400, "The state value is not valid", "/checklists/1/checklistitems/3", new InvalidParams[]{invalidParams});

        throw new InvalidStateException(problemJSON);
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