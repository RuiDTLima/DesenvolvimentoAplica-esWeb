package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.ChecklistTemplates;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutTemplateItem;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.TemplateItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import java.util.Optional;

@RestController
@RequestMapping("/checklisttemplates")
public class ChecklistTemplateController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;

    @Autowired
    private TemplateItemRepository templateItemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ChecklistTemplates getChecklistTemplates(@RequestParam(value = "page", defaultValue = "1") int page){
        Page<ChecklistTemplate> checklistTemplatePage = checklistTemplateRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE));
        return new ChecklistTemplates(checklistTemplatePage);
    }

    @PostMapping
    public String postChecklistTemplate(@RequestBody ChecklistTemplate checklistTemplate) {
        Optional<User> rui = userRepository.findById("Rui");
        checklistTemplate.setUserName(rui.get()); //TODO verificar autenticação
        checklistTemplateRepository.save(checklistTemplate);
        return "Ok";
    }

    @GetMapping("/{checklisttemplate_id}")
    public OutChecklistTemplate getChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id){
        Optional<ChecklistTemplate> checklistTemplate = checklistTemplateRepository.findById(checklisttemplate_id);
        OutChecklistTemplate outChecklistTemplate = new OutChecklistTemplate(checklistTemplate);
        return outChecklistTemplate;
    }

    @GetMapping("/{checklisttemplate_id}/templateitems")
    public String getTemplateItems(
            @PathVariable("checklisttemplate_id") int checklistTemplateId,
            @RequestParam(value = "page", defaultValue = "1") int page){
        if(!checklistTemplateRepository.existsById(checklistTemplateId)) return "Erro";
        checklistTemplateRepository.findById(checklistTemplateId).get().getTemplateItems();
        return "";
    }

    @PostMapping("/{checklisttemplate_id}/templateitems")
    public String postTemplateItem(@PathVariable("checklisttemplate_id") int checklisttemplate_id, @RequestBody TemplateItem templateItem){
        if (!checklistTemplateRepository.existsById(checklisttemplate_id))
            return "Erro";  //TODO lançar erro de id
        templateItemRepository.save(templateItem);
        return "OK";
    }

    @DeleteMapping("/{checklisttemplate_id}")
    public String deleteChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id){
        if (!checklistTemplateRepository.existsById(checklisttemplate_id))
            return "Erro";  //TODO lançar erro de id
        checklistTemplateRepository.deleteById(checklisttemplate_id);
        return "OK";
    }

    @PutMapping("/{checklisttemplate_id}")
    public String updateChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id, @RequestBody ChecklistTemplate checklistTemplate){
        if (!checklistTemplateRepository.existsById(checklisttemplate_id))
            return "Erro";  //TODO lançar erro de id
        checklistTemplateRepository.save(checklistTemplate);
        return "OK";
    }

    @GetMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public OutTemplateItem getTemplateItem(
            @PathVariable("checklisttemplate_id") int checklistTemplateId,
            @PathVariable("templateitem_id") int templateItemId
    ){
        TemplateItem templateItem = templateItemRepository.findById(templateItemId).get();
        return new OutTemplateItem(templateItem);
    }

    @PutMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String updateTemplateItem(
            @PathVariable("checklisttemplate_id") int checklistTemplateId,
            @PathVariable("templateitem_id") int templateItemId,
            @RequestBody TemplateItem templateItem
    ){
        if(!checklistTemplateRepository.existsById(checklistTemplateId) || !templateItemRepository.existsById(templateItemId))
            return "ERROR"; // TODO ???
        templateItemRepository.save(templateItem);
        return "OK";
    }

    @DeleteMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String deleteTemplateItem(
            @PathVariable("checklisttemplate_id") int checklistTemplateId,
            @PathVariable("templateitem_id") int templateItemId
    ){
        if(!checklistTemplateRepository.existsById(checklistTemplateId))
            return "ERROR"; // TODO ???
        templateItemRepository.deleteById(templateItemId);
        return "OK";
    }
}