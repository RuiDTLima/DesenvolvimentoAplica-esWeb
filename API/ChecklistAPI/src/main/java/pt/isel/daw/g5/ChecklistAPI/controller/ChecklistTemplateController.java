package pt.isel.daw.g5.ChecklistAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.RequiresAuthentication;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotAuthenticatedException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotFoundException;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseTemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
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

    @GetMapping(produces = "application/vnd.collection+json")
    public OutChecklistTemplates getChecklistTemplates(@RequestParam(value = "page", defaultValue = "1") int page){
        Page<ChecklistTemplate> checklistTemplatePage = checklistTemplateRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE));
        return new OutChecklistTemplates(checklistTemplatePage);
    }

    @PostMapping
    public String postChecklistTemplate(@RequestBody ChecklistTemplate checklistTemplate) {
        Optional<User> rui = userRepository.findById("Rui");
        checklistTemplate.setUserName(rui.get()); //TODO verificar autenticação
        checklistTemplateRepository.save(checklistTemplate);
        return "Ok";
    }

    @GetMapping(path = "/{checklisttemplate_id}", produces = "application/vnd.siren+json")
    public OutChecklistTemplate getChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                                     HttpServletRequest request){

        log.info(String.format("Trying to retrieve the checklistTemplate %s.", checklisttemplate_id));
        ChecklistTemplate checklistTemplate = validateOperation(checklisttemplate_id, request);

        log.info("ChecklistTemplate found and being returned.");
        return new OutChecklistTemplate(checklistTemplate);
    }

    @DeleteMapping("/{checklisttemplate_id}")
    public String deleteChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                          HttpServletRequest request){

        log.info(String.format("Trying to delete the checklistTemplate %s", checklisttemplate_id));
        validateOperation(checklisttemplate_id, request);

        checklistTemplateRepository.deleteById(checklisttemplate_id);
        log.info("ChecklistTemplate successfully deleted");
        return "OK";
    }

    @PutMapping("/{checklisttemplate_id}")
    public String updateChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                          @RequestBody ChecklistTemplate checklistTemplate,
                                          HttpServletRequest request){
        log.info(String.format("Trying to update the information regarding the checklistTemplate %s", checklisttemplate_id));
        String username = (String) request.getAttribute("Username");
        validateOperation(checklisttemplate_id, request);

        checklistTemplate.setUserName(userRepository.findById(username).get());
        checklistTemplate.setId(checklisttemplate_id);
        checklistTemplateRepository.save(checklistTemplate);
        log.info("ChecklistTemplate successfully updated");
        return "OK";
    }

    @GetMapping(path = "/{checklisttemplate_id}/templateitems", produces = "application/vnd.collection+json")
    public OutTemplateItems getTemplateItems(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                             @RequestParam(value = "page", defaultValue = "0") int page){

        if(!checklistTemplateRepository.existsById(checklistTemplateId)) { /* TODO ERROR HANDLING*/ }
        Page<TemplateItem> itemPage = templateItemRepository.findAllByChecklistTemplate_Id(checklistTemplateId, PageRequest.of(page, PAGE_SIZE));
        return new OutTemplateItems(itemPage, checklistTemplateId);
    }

    @PostMapping("/{checklisttemplate_id}/templateitems")
    public String postTemplateItem(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                   @RequestBody DatabaseTemplateItem templateItem,
                                   HttpServletRequest request){

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (!optionalChecklistTemplate.isPresent())
            throw new NotFoundException();

        if (!optionalChecklistTemplate.get().getUserName().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();
        TemplateItem newItem = new TemplateItem(templateItem.getName(), templateItem.getDescription());
        newItem.setChecklistTemplate(checklistTemplate);
        templateItemRepository.save(newItem);
        return "OK";
    }

    @GetMapping(path = "/{checklisttemplate_id}/templateitems/{templateitem_id}", produces = "application/vnd.siren+json")
    public OutTemplateItem getTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                           @PathVariable("templateitem_id") int templateItemId){

        TemplateItem templateItem = templateItemRepository.findById(templateItemId).get();
        DatabaseTemplateItem databaseTemplateItem = new DatabaseTemplateItem(templateItem);
        return new OutTemplateItem(databaseTemplateItem);
    }

    @PutMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String updateTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                     @PathVariable("templateitem_id") int templateItemId,
                                     @RequestBody TemplateItem templateItem){

        if(!checklistTemplateRepository.existsById(checklistTemplateId) || !templateItemRepository.existsById(templateItemId))
            return "ERROR"; // TODO ???
        templateItemRepository.save(templateItem);
        return "OK";
    }

    @DeleteMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String deleteTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                     @PathVariable("templateitem_id") int templateItemId){

        if(!checklistTemplateRepository.existsById(checklistTemplateId))
            return "ERROR"; // TODO ???
        templateItemRepository.deleteById(templateItemId);
        return "OK";
    }

    private ChecklistTemplate validateOperation(@PathVariable("checklisttemplate_id") int checklisttemplate_id, HttpServletRequest request) {
        log.info("Validating the operation.");
        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (!optionalChecklistTemplate.isPresent()) {
            log.warn(String.format("ChecklistTemplate does not exist."));
            throw new NotFoundException();
        }

        ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();

        if (!checklistTemplate.getUserName().getUsername().equals(username)){
            log.warn(String.format("ChecklistTemplate %s does not belong to the user %s", checklisttemplate_id, username));
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }
        log.info("Validations successful");
        return checklistTemplate;
    }
}