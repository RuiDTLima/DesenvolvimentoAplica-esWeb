package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.outputModel.OutTemplateItem;
import pt.isel.daw.g5.ChecklistAPI.repository.ChecklistTemplateRepository;
import pt.isel.daw.g5.ChecklistAPI.repository.TemplateItemRepository;

import java.util.Optional;

@RestController
@RequestMapping("/checklisttemplates")
public class ChecklistTemplateController {
    private static final int PAGE_SIZE = 10;

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

    @GetMapping("/{checklisttemplate_id}/templateitems")
    public String getTemplateItems(
            @PathVariable("checklisttemplate_id") int checklistTemplateId,
            @RequestParam(value = "page", defaultValue = "1") int page){
        // TODO
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
    public String putChecklistTemplate(@PathVariable("checklisttemplate_id") int checklisttemplate_id, @RequestBody ChecklistTemplate checklistTemplate){
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

    @PostMapping("/{checklisttemplate_id}/templateitems/{templateitem_id}")
    public String putTemplateItem(
            @PathVariable("checklisttemplate_id") int checklistTemplateId,
            @PathVariable("templateitem_id") int templateItemId,
            @RequestBody TemplateItem templateItem
    ){
        if(!checklistTemplateRepository.existsById(checklistTemplateId))
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