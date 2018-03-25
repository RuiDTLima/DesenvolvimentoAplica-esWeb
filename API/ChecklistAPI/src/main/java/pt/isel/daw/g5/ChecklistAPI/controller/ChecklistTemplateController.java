package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.TemplateItemRepository;

import java.util.Optional;

@RestController
@RequestMapping("/checklisttemplates")
public class ChecklistTemplateController {
    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;

    @Autowired
    private TemplateItemRepository templateItemRepository;

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
}