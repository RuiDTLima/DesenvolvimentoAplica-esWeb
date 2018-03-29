package pt.isel.daw.g5.ChecklistAPI.controller;

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
        checklistTemplate.setUser(rui.get()); //TODO verificar autenticação
        checklistTemplateRepository.save(checklistTemplate);
        return "Ok";
    }

    @GetMapping(path = "/{checklisttemplate_id}", produces = "application/vnd.siren+json")
    public OutChecklistTemplate getChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                                     HttpServletRequest request){

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (!optionalChecklistTemplate.isPresent())
            throw new NotFoundException();

        ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();

        if (!checklistTemplate.getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        return new OutChecklistTemplate(checklistTemplate);
    }

    @DeleteMapping("/{checklisttemplate_id}")
    public String deleteChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                          HttpServletRequest request){

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (!optionalChecklistTemplate.isPresent())
            throw new NotFoundException();

        if (!optionalChecklistTemplate.get().getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        checklistTemplateRepository.deleteById(checklisttemplate_id);
        return "OK";
    }

    @PutMapping("/{checklisttemplate_id}")
    public String updateChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                          @RequestBody ChecklistTemplate checklistTemplate,
                                          HttpServletRequest request){

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);

        if (!optionalChecklistTemplate.isPresent())
            throw new NotFoundException();

        if (!optionalChecklistTemplate.get().getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        checklistTemplate.setUser(userRepository.findById(username).get());
        checklistTemplate.setId(checklisttemplate_id);
        checklistTemplateRepository.save(checklistTemplate);
        return "OK";
    }

    @GetMapping(path = "/{checklisttemplate_id}/templateitems", produces = "application/vnd.collection+json")
    public OutTemplateItems getTemplateItems(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             HttpServletRequest request){

        if(!checklistTemplateRepository.existsById(checklistTemplateId))
            throw new NotFoundException();

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklistTemplateId);
        ChecklistTemplate template = optionalChecklistTemplate.get();

        if(!template.getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

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

        if (!optionalChecklistTemplate.get().getUser().getUsername().equals(username)){
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
                                           @PathVariable("templateitem_id") int templateItemId,
                                           HttpServletRequest request){

        if(!checklistTemplateRepository.existsById(checklistTemplateId) || !templateItemRepository.existsById(templateItemId))
            throw new NotFoundException();

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklistTemplateId);
        ChecklistTemplate template = optionalChecklistTemplate.get();

        if(!template.getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        TemplateItem templateItem = templateItemRepository.findById(templateItemId).get();
        DatabaseTemplateItem databaseTemplateItem = new DatabaseTemplateItem(templateItem);
        return new OutTemplateItem(databaseTemplateItem);
    }

    @PutMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String updateTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                     @PathVariable("templateitem_id") int templateItemId,
                                     @RequestBody DatabaseTemplateItem templateItem,
                                     HttpServletRequest request){

        if(!checklistTemplateRepository.existsById(checklistTemplateId) || !templateItemRepository.existsById(templateItemId))
            throw new NotFoundException();

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklistTemplateId);
        ChecklistTemplate template = optionalChecklistTemplate.get();

        if(!template.getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        TemplateItem updatedItem = templateItemRepository.findById(templateItemId).get();
        updatedItem.setName(templateItem.getName());
        updatedItem.setDescription(templateItem.getDescription());
        templateItemRepository.save(updatedItem);
        return "OK";
    }

    @DeleteMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String deleteTemplateItem(@PathVariable("checklisttemplate_id") int checklistTemplateId,
                                     @PathVariable("templateitem_id") int templateItemId,
                                     HttpServletRequest request){

        if(!checklistTemplateRepository.existsById(checklistTemplateId))
            throw new NotFoundException();

        String username = (String) request.getAttribute("Username");
        Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findById(checklistTemplateId);
        ChecklistTemplate template = optionalChecklistTemplate.get();

        if(!template.getUser().getUsername().equals(username)){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Invalid User.", 403, "The user provided does not have access to this list", request.getRequestURI(), new InvalidParams[]{notIncludedUser});
            throw new NotAuthenticatedException(problemJSON);
        }

        templateItemRepository.deleteById(templateItemId);
        return "OK";
    }
}