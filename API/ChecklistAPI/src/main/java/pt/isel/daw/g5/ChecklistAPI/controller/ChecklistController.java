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
import pt.isel.daw.g5.ChecklistAPI.exceptions.ForbiddenException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.InvalidStateException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotFoundException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.*;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistItems;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklists;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
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
    public OutChecklists getChecklists(@RequestParam(value = "page", defaultValue = "0") int page,
                                       HttpServletRequest request){

        String username = (String) request.getAttribute("Username");
        User user = userRepository.findById(username).get();
        Page<Checklist> checklistPage = checklistRepository.findAllByUsername(user, PageRequest.of(page, PAGE_SIZE));
        return new OutChecklists(checklistPage);
    }

    /**
     * Método para criar uma checklist. Esta operação pode ser feita com ou sem o uso de um checklistTemplate, sendo que
     * ai o campo checklistTemplate_id tem um valor diferente de 0. A única diferença na operação é o facto de caso esta
     * seja realizada com o uso de um checklistTemplate a nova checklist deve ter o seu identificador no campo
     * checklisttemplate_id
     * @param databaseChecklist Entidade que representa uma checklist tal como está na base de dados.
     * @param request
     * @return
     */
    @PostMapping
    @Transactional
    public ResponseEntity postChecklist(@RequestBody DatabaseChecklist databaseChecklist,
                                        HttpServletRequest request) {

        String username = (String) request.getAttribute("Username");
        log.info(String.format("Creating a new checklist for the user %s", username));

        int checklistTemplateId = databaseChecklist.getChecklistTemplateId();

        String completionDate =  databaseChecklist.getCompletionDate();

        try{
            ZonedDateTime.parse(completionDate);
        }catch (DateTimeParseException ex){
            log.warn(String.format("The completion date %s does not follow the specification ISO 8601", completionDate));
            InvalidParams invalidDate = new InvalidParams("completion_date", "The completion date does not follow the specification ISO 8601", completionDate);
            ProblemJSON problemJSON = new ProblemJSON("/invalid-date-format", "Invalid Date Format", 400, String.format("The completion date %s does not follow the specification ISO 8601", completionDate), request.getRequestURI(), new InvalidParams[]{invalidDate});
            throw new InvalidStateException(problemJSON);
        }

        if (checklistTemplateId != 0){ // Significa que o checklist é gerado através de um checklisttemplate
            log.info("The checklist is being created with the help of a ChecklistTemplate");

            Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklistTemplateId);
            if (!optionalChecklistTemplate.isPresent()) {
                log.warn("The ChecklistTemplate being used does not exist");
                throw new NotFoundException(String.format("ChecklistTemplate %s does not exist", checklistTemplateId));
            }

            ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();

            if (!checklistTemplate.isUsable()){
                log.warn(String.format("The checklistTemplate %s is no longer valid to use", checklistTemplate.getId()));
                InvalidParams notValidTemplate = new InvalidParams("usable", "Template is not valid to use");
                ProblemJSON problemJSON = new ProblemJSON("/invalid-template", "Invalid Template.", 403, "The template can longer be used to create a checklist since it has been deleted", request.getRequestURI(), new InvalidParams[]{notValidTemplate});
                throw new ForbiddenException(problemJSON);
            }
            if (!checklistTemplate.getUser().getUsername().equals(username)) {
                log.warn(String.format("The checklistTemplate %s does not belong to the user %s", checklistTemplate.getId(), username));
                InvalidParams notValidUser = new InvalidParams("username", "username is invalid");
                ProblemJSON problemJSON = new ProblemJSON("/forbidden-error", "Invalid User.", 403, "The user provided does not have access to this template", request.getRequestURI(), new InvalidParams[]{notValidUser});
                throw new ForbiddenException(problemJSON);
            }

            Checklist checklist = new Checklist(checklistTemplate.getName(), completionDate);

            checklist.setChecklistTemplate(checklistTemplate);
            checklist.setUsername(userRepository.findById(username).get());
            Checklist createdChecklist = checklistRepository.save(checklist);

            log.info("The checklist was added to the database, now to insert the items");

            List<TemplateItem> templateItems = checklistTemplate.getTemplateItems();

            templateItems.forEach(templateItem -> {
                ChecklistItem checklistItem = new ChecklistItem(templateItem.getName(), templateItem.getDescription());
                checklistItem.setChecklist(createdChecklist);
                checklistItemRepository.save(checklistItem);
            });

            log.info("The items were added to the database");
        } else{
            log.info("The checklist is create without the help of a checklistTemplate");

            Checklist checklist = new Checklist(databaseChecklist.getName(), completionDate);
            checklist.setUsername(userRepository.findById(username).get());
            checklistRepository.save(checklist);
        }

        log.info("Checklist successfully created.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{checklist_id}", produces = "application/vnd.siren+json")
    @Transactional
    public OutChecklist getChecklist(@PathVariable("checklist_id") int checklistId,
                                     HttpServletRequest request){

        Checklist checklist = validateOperation(checklistId, request);
        boolean uncompleted = checklist.getInChecklistItems().stream().anyMatch(item -> item.getState().equals("uncompleted"));
        DatabaseChecklist databaseChecklist = new DatabaseChecklist(checklist);
        databaseChecklist.setCompletionState(uncompleted ? "uncompleted" : "completed");
        return new OutChecklist(databaseChecklist);
    }

    @PutMapping("/{checklist_id}")
    public ResponseEntity updateChecklist(@PathVariable("checklist_id") int checklistId,
                                          @RequestBody DatabaseChecklist updatedChecklist,
                                          HttpServletRequest request){

        Checklist checklist = validateOperation(checklistId, request);
        String completionDate =  updatedChecklist.getCompletionDate();

        try{
            ZonedDateTime.parse(completionDate);
        }catch (DateTimeParseException ex){
            log.warn(String.format("The completion date %s does not follow the specification ISO 8601", completionDate));
            InvalidParams invalidDate = new InvalidParams("completion_date", "The completion date does not follow the specification ISO 8601", completionDate);
            ProblemJSON problemJSON = new ProblemJSON("/invalid-date-format", "Invalid Date Format", 400, String.format("The completion date %s does not follow the specification ISO 8601", completionDate), request.getRequestURI(), new InvalidParams[]{invalidDate});
            throw new InvalidStateException(problemJSON);
        }

        checklist.setName(updatedChecklist.getName());
        checklist.setCompletionDate(updatedChecklist.getCompletionDate());
        checklistRepository.save(checklist);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{checklist_id}")
    @Transactional
    public ResponseEntity deleteChecklist(@PathVariable("checklist_id") int checklistId,
                                          HttpServletRequest request){

        Checklist checklist = validateOperation(checklistId, request);
        checklistItemRepository.deleteAllByChecklistId(checklistId);
        checklistRepository.delete(checklist);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{checklist_id}/checklistitems", produces = "application/vnd.collection+json")
    public OutChecklistItems getChecklistItems(@PathVariable("checklist_id") int checklistId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               HttpServletRequest request) {

        log.info(String.format("Trying to retrive items from the checklist %s", checklistId));
        validateOperation(checklistId, request);
        Page<ChecklistItem> checklistItemPage = checklistItemRepository.findAllByChecklistId(checklistId, PageRequest.of(page, PAGE_SIZE));
        log.info("Checklist items found and being returned");
        return new OutChecklistItems(checklistItemPage, checklistId);
    }

    @PostMapping("/{checklist_id}/checklistitems")
    public ResponseEntity addChecklistItem(@PathVariable("checklist_id") int checklistId,
                                           @RequestBody DatabaseChecklistItem databaseChecklistItem,
                                           HttpServletRequest request){

        log.info(String.format("Trying to add a new item to the checklist %s", checklistId));
        Checklist checklist = validateOperation(checklistId, request);

        ChecklistItem newItem = new ChecklistItem(databaseChecklistItem.getName(), databaseChecklistItem.getDescription());
        newItem.setChecklist(checklist);

        checklistItemRepository.save(newItem);
        log.info("Checklist item successfully added");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Método para apresentar um item pertencente a uma checklist. Sendo que caso o item identificado por
     * checklistitem_id não pertença à checklist identificada por checklist_id ou não exista, um erro é retornado.
     * @param checklist_id Id da checklist
     * @param checklistitem_id Id do item da checklist
     * @param request
     * @return
     */
    @GetMapping(path = "/{checklist_id}/checklistitems/{checklistitem_id}", produces = "application/vnd.siren+json")
    @Transactional
    public OutChecklistItem getChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                             @PathVariable("checklistitem_id") int checklistitem_id,
                                             HttpServletRequest request){

        log.info(String.format("Begin operation to retrieve the item %s from the checklist %s", checklistitem_id, checklist_id));
        Optional<ChecklistItem> item = checklistItemRepository.findById(checklistitem_id);
        if(item.isPresent()) {
            log.info("Found the item. Returning.");
            return new OutChecklistItem(item.get());
        }
        String message = String.format("The item %s does not belong to the checklist %s, or it does not exist", checklistitem_id, checklist_id);
        log.warn(message);
        throw new NotFoundException(message);
    }

    /**
     * Método para actualizar a informação de um item de uma checklist. Caso o item identificado por checklistitem_id
     * não pertença à checklist identificada por checklist_id ou não exista, um erro é retornado.
     * @param checklist_id Id da checklist
     * @param checklistitem_id Id do item da checklist
     * @param databaseChecklistItem A representação do item com a informação actualizada
     * @param request
     * @return
     */
    @PutMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public ResponseEntity putChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                           @PathVariable("checklistitem_id") int checklistitem_id,
                                           @RequestBody DatabaseChecklistItem databaseChecklistItem,
                                           HttpServletRequest request){

        log.info(String.format("Updating the item %s from the checklist %s.", checklistitem_id, checklist_id));
        validateOperation(checklist_id, request);
        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findById(checklistitem_id);

        if (!optionalChecklistItem.isPresent() || optionalChecklistItem.get().getChecklist().getId() != checklist_id) {
            String message = String.format("The item %s does not belong to the checklist %s, or it does not exist", checklistitem_id, checklist_id);
            log.warn(message);
            throw new NotFoundException(message);
        }

        log.info("Verify if state is valid");

        String state = databaseChecklistItem.getState();

        if (!state.equals("completed") && !state.equals("uncompleted")){
            log.warn("Checklist item's state must be either 'completed' or 'uncompleted'");
            InvalidParams invalidState = new InvalidParams("state","state must be either 'completed' or 'uncompleted'", "completed");
            ProblemJSON problemJSON = new ProblemJSON("/invalid-state", "The Checklist state is invalid", 400, "The Checklist item can't be changed to the new values", request.getRequestURI(), new InvalidParams[]{invalidState});
            throw new InvalidStateException(problemJSON);
        }

        ChecklistItem checklistItem = optionalChecklistItem.get();
        checklistItem.setName(databaseChecklistItem.getName());
        checklistItem.setDescription(databaseChecklistItem.getDescription());
        checklistItem.setState(databaseChecklistItem.getState());
        checklistItemRepository.save(checklistItem);

        log.info("Item updated.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Método para eliminar um item de uma checklist. Caso o item identificado por checklistitem_id não pertença à
     * checklist identificada por checklist_id ou não exista é retornado um erro.
     * @param checklist_id Id da checklist
     * @param checklistitem_id Id do item da checklist
     * @param request
     * @return
     */
    @DeleteMapping("/{checklist_id}/checklistitems/{checklistitem_id}")
    public ResponseEntity<String> deleteChecklistItem(@PathVariable("checklist_id") int checklist_id,
                                                      @PathVariable("checklistitem_id") int checklistitem_id,
                                                      HttpServletRequest request){

        log.info(String.format("Deleting the item %s from the checklist %s", checklistitem_id, checklist_id));

        validateOperation(checklist_id, request);

        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findById(checklistitem_id);
        if (!optionalChecklistItem.isPresent() || optionalChecklistItem.get().getChecklist().getId() != checklist_id) {
            String message = String.format("The item %s does not belong to the checklist %s, or it does not exist", checklistitem_id, checklist_id);
            log.warn(message);
            throw new NotFoundException(message);
        }

        checklistItemRepository.deleteById(checklistitem_id);
        log.info("Item deleted.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Método para validar se a operação reune as condições necessárias para prosseguir. Sendo essas a de a checklist
     * existir e pertencer ao utilizador autenticado.
     * @param checklist_id
     * @param request
     * @return
     */
    private Checklist validateOperation(int checklist_id, HttpServletRequest request) {
        log.info("Validating the operation");
        String username = (String) request.getAttribute("Username");
        Optional<Checklist> optionalChecklist = checklistRepository.findById(checklist_id);

        if (!optionalChecklist.isPresent()) {
            String message = String.format("The checklist %s does not exist.", checklist_id);
            log.warn(message);
            throw new NotFoundException(message);
        }

        Checklist checklist = optionalChecklist.get();

        if (!checklist.getUsername().getUsername().equals(username)){
            log.warn(String.format("The checklist %s does not belong to the user %s", checklist_id, username));
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/forbidden-error", "Invalid User.", 403, "The user provided does not have access to this checklist", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new ForbiddenException(problemJSON);
        }
        log.info("Validation was successful.");
        return checklist;
    }
}