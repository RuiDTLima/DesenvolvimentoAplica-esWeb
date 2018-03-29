package pt.isel.daw.g5.ChecklistAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.RequiresAuthentication;
import pt.isel.daw.g5.ChecklistAPI.exceptions.ForbiddenException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotFoundException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseTemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistTemplates;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutTemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutTemplateItems;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.TemplateItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/checklisttemplates")
@RequiresAuthentication
public class ChecklistTemplateController {
    private static final Logger log = LoggerFactory.getLogger(ChecklistTemplateController.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;

    @Autowired
    private TemplateItemRepository templateItemRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Finds and returns a collection of checklist templates
     * @param page
     * @return
     */
    @GetMapping(produces = "application/vnd.collection+json")
    public OutChecklistTemplates getChecklistTemplates(@RequestParam(value = "page", defaultValue = "1") int page){
        Page<ChecklistTemplate> checklistTemplatePage = checklistTemplateRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE));
        return new OutChecklistTemplates(checklistTemplatePage);
    }

    /**
     * Creates a new checklist template
     * @param checklistTemplate the new checklist template to be added
     * @param request
     * @return
     */
    @PostMapping
    public String postChecklistTemplate(@RequestBody ChecklistTemplate checklistTemplate,
                                        HttpServletRequest request) {
        String username = (String) request.getAttribute("Username");
        checklistTemplate.setUser(userRepository.findById(username).get());
        checklistTemplateRepository.save(checklistTemplate);
        return "OK";
    }

    /**
     * Método para apresentar a informação referente a um checklistTemplate identificado por checklisttemplate_id.
     * @param checklisttemplate_id
     * @param request
     * @return
     */
    @GetMapping(path = "/{checklisttemplate_id}", produces = "application/vnd.siren+json")
    public OutChecklistTemplate getChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                                     HttpServletRequest request){

        log.info(String.format("Trying to retrieve the checklistTemplate %s.", checklisttemplate_id));
        ChecklistTemplate checklistTemplate = validateOperation(checklisttemplate_id, request);

        log.info("ChecklistTemplate found and being returned.");
        return new OutChecklistTemplate(checklistTemplate);
    }

    /**
     * Método para apagar um checklistTemplate identificado por checklisttemplate_id
     * @param checklisttemplate_id
     * @param request
     * @return
     */
    @DeleteMapping("/{checklisttemplate_id}")
    public ResponseEntity deleteChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                                  HttpServletRequest request){

        log.info(String.format("Trying to delete the checklistTemplate %s", checklisttemplate_id));
        validateOperation(checklisttemplate_id, request);

        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (optionalChecklistTemplate.isPresent()){
            ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();
            checklistTemplate.setUsable(false);
            checklistTemplateRepository.save(checklistTemplate);
        }
        else
            checklistTemplateRepository.deleteById(checklisttemplate_id);
        log.info("ChecklistTemplate successfully deleted");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Método para actualizar a informação de um checklistTemplate identificado por checklisttemplate_id. O
     * databaseChecklistTemplate é uma entidade que representa o template com a informação actualizada.
     * @param checklisttemplate_id
     * @param databaseChecklistTemplate
     * @param request
     * @return
     */
    @PutMapping("/{checklisttemplate_id}")
    public ResponseEntity updateChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                                  @RequestBody DatabaseChecklistTemplate databaseChecklistTemplate,
                                                  HttpServletRequest request){
        log.info(String.format("Trying to update the information regarding the checklistTemplate %s", checklisttemplate_id));
        String username = (String) request.getAttribute("Username");

        ChecklistTemplate checklistTemplate = validateOperation(checklisttemplate_id, request);

        checklistTemplate.setName(databaseChecklistTemplate.getName());
        checklistTemplateRepository.save(checklistTemplate);
        log.info("ChecklistTemplate successfully updated");

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Returns the items belonging in the checklist template identified by checklistTemplateId
     * @param checklistTemplateId the identifier of the checklist template
     * @param page
     * @param request
     * @return
     */
    @GetMapping(path = "/{checklisttemplate_id}/templateitems", produces = "application/vnd.collection+json")
    public OutTemplateItems getTemplateItems(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             HttpServletRequest request){

        log.info(String.format("Trying to retrive items from the checklistTemplate %s", checklistTemplateId));
        validateOperation(checklistTemplateId, request);

        Page<TemplateItem> itemPage = templateItemRepository.findAllByChecklistTemplate_Id(checklistTemplateId, PageRequest.of(page, PAGE_SIZE));
        log.info("ChecklistTemplate items found and being returned");
        return new OutTemplateItems(itemPage, checklistTemplateId);
    }

    /**
     * Adds a new template item to a checklist template
     * @param checklisttemplate_id the identifier of the checklist template
     * @param templateItem the identifier of the template item
     * @param request
     * @return
     */
    @PostMapping("/{checklisttemplate_id}/templateitems")
    public String postTemplateItem(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                   @RequestBody DatabaseTemplateItem templateItem,
                                   HttpServletRequest request){

        log.info(String.format("Trying to add a new item to the checklistTemplate %s", checklisttemplate_id));
        ChecklistTemplate checklistTemplate = validateOperation(checklisttemplate_id, request);

        TemplateItem newItem = new TemplateItem(templateItem.getName(), templateItem.getDescription());
        newItem.setChecklistTemplate(checklistTemplate);
        templateItemRepository.save(newItem);
        log.info("ChecklistTemplate item successfully added");
        return "OK";
    }

    /**
     * Finds and returns a template item
     * @param checklistTemplateId the identifier of the checklist template
     * @param templateItemId the identifier of the template item
     * @param request
     * @return
     */
    @GetMapping(path = "/{checklisttemplate_id}/templateitems/{templateitem_id}", produces = "application/vnd.siren+json")
    public OutTemplateItem getTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                           @PathVariable("templateitem_id") int templateItemId,
                                           HttpServletRequest request){

        log.info(String.format("Trying to retrive an item from the checklistTemplate %s", checklistTemplateId));
        ChecklistTemplate template = validateOperation(checklistTemplateId, request);
        if(!templateItemRepository.existsById(templateItemId) ||
                !template.getTemplateItems().stream().anyMatch(item -> item.getId() == templateItemId)){
            throw new NotFoundException();
        }

        TemplateItem templateItem = templateItemRepository.findById(templateItemId).get();
        DatabaseTemplateItem databaseTemplateItem = new DatabaseTemplateItem(templateItem);
        log.info("ChecklistTemplate item retrieved and being returned");
        return new OutTemplateItem(databaseTemplateItem);
    }

    /**
     * Updates a template item
     * @param checklistTemplateId the identifier of the checklist template
     * @param templateItemId the identifier of the template item
     * @param templateItem the item containing the fields to be updated
     * @param request
     * @return
     */
    @PutMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String updateTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                     @PathVariable("templateitem_id") int templateItemId,
                                     @RequestBody DatabaseTemplateItem templateItem,
                                     HttpServletRequest request){

        log.info(String.format("Trying to update an item from the checklistTemplate %s", checklistTemplateId));
        ChecklistTemplate template = validateOperation(checklistTemplateId, request);
        if(!templateItemRepository.existsById(templateItemId) ||
           !template.getTemplateItems().stream().anyMatch(item -> item.getId() == templateItemId)){
            throw new NotFoundException();
        }

        TemplateItem updatedItem = templateItemRepository.findById(templateItemId).get();
        updatedItem.setName(templateItem.getName());
        updatedItem.setDescription(templateItem.getDescription());
        templateItemRepository.save(updatedItem);
        log.info("ChecklistTemplate item successfully updated");
        return "OK";
    }

    /**
     * Deletes a template item from a checklist template
     * @param checklistTemplateId the identifier of the checklist template
     * @param templateItemId the identifier of the template item
     * @param request
     * @return
     */
    @DeleteMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String deleteTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                     @PathVariable("templateitem_id") int templateItemId,
                                     HttpServletRequest request){

        log.info(String.format("Trying to delete an item from the checklistTemplate %s", checklistTemplateId));
        ChecklistTemplate template = validateOperation(checklistTemplateId, request);
        if(!templateItemRepository.existsById(templateItemId) ||
                !template.getTemplateItems().stream().anyMatch(item -> item.getId() == templateItemId)){
            throw new NotFoundException();
        }

        templateItemRepository.deleteById(templateItemId);
        log.info("ChecklistTemplate successfully deleted");
        return "OK";
    }

    /**
     * Método para validar se a operação reune as condições necessárias para prosseguir. Sendo essas a de o template
     * existir e pertence ao utilizador autenticado.
     * @param checklisttemplate_id
     * @param request
     * @return
     */
    private ChecklistTemplate validateOperation(@PathVariable("checklisttemplate_id") int checklisttemplate_id, HttpServletRequest request) {
        log.info("Validating the operation.");
        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (!optionalChecklistTemplate.isPresent()) {
            log.warn(String.format("ChecklistTemplate does not exist."));
            throw new NotFoundException();
        }

        ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();

        if (!checklistTemplate.getUser().getUsername().equals(username)){
            log.warn(String.format("ChecklistTemplate %s does not belong to the user %s", checklisttemplate_id, username));
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new ForbiddenException(problemJSON);
        }
        log.info("Validations successful");
        return checklistTemplate;
    }
}