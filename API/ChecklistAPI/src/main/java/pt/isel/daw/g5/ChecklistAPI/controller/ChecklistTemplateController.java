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
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.TemplateItemRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/checklisttemplates")
public class ChecklistTemplateController {
    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;

    @Autowired
    private TemplateItemRepository templateItemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ChecklistTemplates getChecklistTemplates(@RequestParam(value = "page", defaultValue = "1") int page){
        Page<ChecklistTemplate> checklistTemplatePage = checklistTemplateRepository.findAll(PageRequest.of(page - 1, 10));
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
    public String putChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id, @RequestBody ChecklistTemplate checklistTemplate){
        if (!checklistTemplateRepository.existsById(checklisttemplate_id))
            return "Erro";  //TODO lançar erro de id
        checklistTemplateRepository.save(checklistTemplate);
        return "OK";
    }

    @PutMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String putChecklistTemplateItem(@PathVariable("checklisttemplate_id") int checklisttemplate_id,
                                   @PathVariable("templateitem_id") int templateitem_id, @RequestBody TemplateItem templateItem){

        if (!checklistTemplateRepository.existsById(checklisttemplate_id) || !templateItemRepository.existsById(templateitem_id))
            return "Não existe";    //TODO enviar erro de id
        templateItemRepository.save(templateItem); //TODO se quiser alterar apenas um campo
        return "OK";
    }
}