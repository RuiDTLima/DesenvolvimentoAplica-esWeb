package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public class OutChecklistTemplate {
    private Siren<ChecklistTemplate> siren;

    public Siren<ChecklistTemplate> getSiren() {
        return siren;
    }

    public void setSiren(Siren<ChecklistTemplate> siren) {
        this.siren = siren;
    }

    public OutChecklistTemplate(Optional<ChecklistTemplate> optionalChecklistTemplate) {
        ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();
        siren = new Siren<>(new String[]{"checklisttemplate"}, checklistTemplate, produceEntity(checklistTemplate), produceActions(checklistTemplate), produceLinks(checklistTemplate));
    }

    private Entity[] produceEntity(ChecklistTemplate checklistTemplate) {
        int checklistTemplateId = checklistTemplate.getId();
        String[] _class = new String[]{"templateItem"};
        String[] rel = new String[]{"/checklisttemplate/" + checklistTemplateId};
        String href = String.format("/checklisttemplate/%s/templateitems", checklistTemplateId);
        return new Entity[]{new Entity(_class, rel, href)};
    }

    private Action[] produceActions(ChecklistTemplate checklistTemplate) {
        return new Action[]{producePostTemplateItem(checklistTemplate), produceDelete(checklistTemplate), producePut(checklistTemplate), producePostChecklist(checklistTemplate)};
    }

    private Action producePostTemplateItem(ChecklistTemplate checklistTemplate) {
        return new Action("add-templateitem", "Add Template Item", "POST", String.format("/checklisttemplates/%s/templateitems", checklistTemplate.getId()), "application/json", producePostTemplateItemFields(checklistTemplate));
    }

    private Action produceDelete(ChecklistTemplate checklistTemplate) {
        return new Action("delete-checklisttemplate", "Delete OutChecklist Template", "DELETE", "/checklisttemplates/" + checklistTemplate.getId(), "application/x-www-form-urlencoded", produceDeleteFields(checklistTemplate));
    }

    private Action producePut(ChecklistTemplate checklistTemplate) {
        return new Action("update-checklisttemplate", "Update OutChecklist Template", "PUT", "/checklisttemplates/" + checklistTemplate.getId(), "application/json", producePutFields(checklistTemplate));
    }

    private Action producePostChecklist(ChecklistTemplate checklistTemplate) {
        return new Action("create-checklist", "Create OutChecklist", "POST", "/checklists", "application/json", producePostChecklistFields(checklistTemplate));
    }

    private Field[] producePostTemplateItemFields(ChecklistTemplate checklistTemplate) {
        Field id = new Field("id", "hidden", Integer.toString(checklistTemplate.getId()), "Checkslist Template Id");
        Field name = new Field("name", "text", "Name");
        Field description = new Field("description", "text", "Description");
        return new Field[]{id, name, description};
    }

    private Field[] produceDeleteFields(ChecklistTemplate checklistTemplate) {
        Field id = new Field("id", "hidden", Integer.toString(checklistTemplate.getId()), "Checkslist Template Id");
        return new Field[]{id};
    }

    private Field[] producePutFields(ChecklistTemplate checklistTemplate) {
        Field id = new Field("id", "hidden", Integer.toString(checklistTemplate.getId()), "Checkslist Template Id");
        Field name = new Field("name", "text", "Name");
        return new Field[]{id, name};
    }

    private Field[] producePostChecklistFields(ChecklistTemplate checklistTemplate) {
        Field checklisttemplate_id = new Field("checklisttemplate_id", "hidden", Integer.toString(checklistTemplate.getId()), "OutChecklist Id");
        Field name = new Field("name", "text", "Name");
        Field completion_date = new Field("completion_date", "text", "Completion Date");
        return new Field[]{checklisttemplate_id, name, completion_date};
    }

    private SirenLink[] produceLinks(ChecklistTemplate checklistTemplate) {
        SirenLink self = new SirenLink(new String[]{"self"}, String.format("/checklisttemplates/%s", checklistTemplate.getId()));
        //TODO adicionar os links next e previous
        return new SirenLink[]{self};
    }
}