package pt.isel.daw.g5.ChecklistAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.RequiresAuthentication;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotAuthenticatedException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotFoundException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.ChecklistItems;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklists;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/checklists")
@RequiresAuthentication
public class ChecklistController {
    private static final Logger log = LoggerFactory.getLogger(ChecklistController.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;

    @GetMapping(produces = "application/vnd.collection+json")
    public OutChecklists getChecklists(@RequestParam(value = "page", defaultValue = "0") int page){
        Page<Checklist> checklistPage = checklistRepository.findAll(PageRequest.of(page, PAGE_SIZE));
        return new OutChecklists(checklistPage);
        //throw new InvalidStateException("invalid state");
    }

    @PostMapping
    public String postChecklist(@RequestBody DatabaseChecklist databaseChecklist,
                                HttpServletRequest request) {

        String username = (String) request.getAttribute("Username");
        log.info(String.format("Creating a new checklist for the user %s", username));

        Checklist checklist = new Checklist(databaseChecklist.getName(), databaseChecklist.getCompletionDate());

        if (databaseChecklist.getChecklisttemplate_id() != 0){ // Significa que o checklist é gerado através de um checklisttemplate
            log.info("The checklist is being created with the help of a ChecklistTemplate");
            Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(databaseChecklist.getChecklisttemplate_id());
            if (!optionalChecklistTemplate.isPresent()) {
                log.warn("The ChecklistTemplate being used does not exist");
                throw new NotFoundException();
            }
            ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();

            if (!checklistTemplate.getUserName().getUsername().equals(username)) {
                log.warn(String.format("The checklistTemplate %s does not belong to the user %s", checklistTemplate.getId(), username));
                InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
                ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this template", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
                throw new NotAuthenticatedException(problemJSON);
            }

            checklist.setChecklistTemplate(checklistTemplate);
        }

        checklist.setUsername(userRepository.findById(username).get());
        checklistRepository.save(checklist);

        log.info("Checklist successfully created.");
        return "Ok";
    }

    @GetMapping(path = "/{checklist_id}", produces = "application/vnd.siren+json")
    public OutChecklist getChecklist(@PathVariable("checklist_id") int checklistId){
        Optional<Checklist> checklistOptional = checklistRepository.findById(checklistId);
        Checklist checklist = checklistOptional.get();
        return new OutChecklist(checklist);
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

    @GetMapping(path = "/{checklist_id}/checklistitems", produces = "application/vnd.collection+json")
    public ChecklistItems getChecklistItems(@PathVariable("checklist_id") int checklistId) {
        if(!checklistRepository.existsById(checklistId)) return null;
//            throw new ChangeSetPersister.NotFoundException();
//        Page<OutChecklistItem> checklistItemPage = checklistRepository.findById(checklistId).get().getInChecklistItems();
        return null; //TODO paging
    }

    @PostMapping("/{checklist_id}/checklistitems")
    public String addChecklistItem(@PathVariable("checklist_id") int checklistId,
                                   @RequestBody ChecklistItem checklistItem){

        if(!checklistRepository.existsById(checklistId))
            return "ERROR"; // TODO
        checklistItemRepository.save(checklistItem);
        return "OK";
    }

    @GetMapping(path = "/{checklist_id}/checklistitems/{checklistitem_id}", produces = "application/vnd.siren+json")
    @Transactional //TODO validar uso da anotação
    public OutChecklistItem getChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                             @PathVariable("checklistitem_id") int checklistitem_id,
                                             HttpServletRequest request){

        log.info(String.format("Begin operation to retrieve the item %s from the checklist %s", checklistitem_id, checklist_id));
        Checklist checklist = validateOperation(checklist_id, request);

        for (ChecklistItem nextItem : checklist.getInChecklistItems()) {
            if (nextItem.getId() == checklistitem_id) {
                log.info("Found the item. Returning.");
                return new OutChecklistItem(nextItem);
            }
        }
        log.warn(String.format("The item %s does not belong to the checklist %s, or it does not exist", checklistitem_id, checklist_id));
        throw new NotFoundException();
    }

    @PutMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public String putChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                   @PathVariable("checklistitem_id") int checklistitem_id,
                                   @RequestBody ChecklistItem checklistItem,
                                   HttpServletRequest request){

        log.info(String.format("Updating the item %s from the checklist %s.", checklistitem_id, checklist_id));
        Checklist checklist = validateOperation(checklist_id, request);

        if (!checklistItemRepository.existsById(checklistitem_id) || checklistItem.getChecklistId().getId() != checklist_id) {
            log.warn(String.format("The item %s does not belong to the checklist %s, or it does not exist", checklistitem_id, checklist_id));
            throw new NotFoundException();
        }

        checklistItem.setId(checklistitem_id);
        checklistItem.setChecklistId(checklist);
        checklistItemRepository.save(checklistItem);
        log.info("Item updated.");
        return "OK";
    }

    @DeleteMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public ResponseEntity<String> deleteChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                                      @PathVariable("checklistitem_id") int checklistitem_id,
                                                      HttpServletRequest request){

        log.info(String.format("Deleting the item %s from the checklist %s", checklistitem_id, checklist_id));

        validateOperation(checklist_id, request);

        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findById(checklistitem_id);
        if (!optionalChecklistItem.isPresent() || optionalChecklistItem.get().getChecklistId().getId() != checklist_id) {
            log.warn(String.format("The item %s does not belong to the checklist %s, or it does not exist", checklistitem_id, checklist_id));
            throw new NotFoundException();
        }

        checklistItemRepository.deleteById(checklistitem_id);
        log.info("Item deleted.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Checklist validateOperation(@PathVariable("checklist_id") int checklist_id, HttpServletRequest request) {
        log.info("Validating the operation");
        String username = (String) request.getAttribute("Username");
        Optional<Checklist> optionalChecklist = checklistRepository.findById(checklist_id);

        if (!optionalChecklist.isPresent()) {
            log.warn(String.format("The checklist %s does not exist.", checklist_id));
            throw new NotFoundException();
        }

        Checklist checklist = optionalChecklist.get();

        if (!checklist.getUsername().getUsername().equals(username)){
            log.warn(String.format("The checklist %s does not belong to the user %s", checklist_id, username));
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this checklist", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }
        log.info("Validation was successful.");
        return checklist;
    }
}