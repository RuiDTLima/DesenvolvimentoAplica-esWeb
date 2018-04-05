package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class OutChecklistTemplate {
    @JsonProperty("class")
    private String[] _class;

    @JsonProperty("properties")
    private DatabaseChecklistTemplate properties;

    @JsonProperty("entities")
    private Entity[] entities;

    @JsonProperty("actions")
    private Action[] actions;

    @JsonProperty("links")
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public DatabaseChecklistTemplate getProperties() {
        return properties;
    }

    public void setProperties(DatabaseChecklistTemplate properties) {
        this.properties = properties;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public SirenLink[] getSirenLinks() {
        return sirenLinks;
    }

    public void setSirenLinks(SirenLink[] sirenLinks) {
        this.sirenLinks = sirenLinks;
    }

    public OutChecklistTemplate(ChecklistTemplate checklistTemplate) {
        _class = new String[]{"checklisttemplate"};
        properties = new DatabaseChecklistTemplate(checklistTemplate);
        entities = produceEntity(checklistTemplate);
        actions = produceActions(checklistTemplate);
        sirenLinks = produceLinks(checklistTemplate);
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
        return new Action("delete-checklisttemplate", "Delete OutChecklist Template", "DELETE", "/checklisttemplates/" + checklistTemplate.getId(), "application/x-www-form-urlencoded", null);
    }

    private Action producePut(ChecklistTemplate checklistTemplate) {
        return new Action("update-checklisttemplate", "Update OutChecklist Template", "PUT", "/checklisttemplates/" + checklistTemplate.getId(), "application/json", producePutFields(checklistTemplate));
    }

    private Action producePostChecklist(ChecklistTemplate checklistTemplate) {
        return new Action("create-checklist", "Create OutChecklist", "POST", "/checklists", "application/json", producePostChecklistFields(checklistTemplate));
    }

    private Field[] producePostTemplateItemFields(ChecklistTemplate checklistTemplate) {
        Field id = new Field("checklisttemplate_id", "hidden", Integer.toString(checklistTemplate.getId()), "Checklist Template Id");
        Field name = new Field("name", "text", "Name");
        Field description = new Field("description", "text", "Description");
        return new Field[]{id, name, description};
    }

    private Field[] producePutFields(ChecklistTemplate checklistTemplate) {
        Field id = new Field("checklisttemplate_id", "hidden", Integer.toString(checklistTemplate.getId()), "Checklist Template Id");
        Field name = new Field("name", "text", "Name");
        return new Field[]{id, name};
    }

    private Field[] producePostChecklistFields(ChecklistTemplate checklistTemplate) {
        Field checklisttemplate_id = new Field("checklisttemplate_id", "hidden", Integer.toString(checklistTemplate.getId()), "Checklist Id");
        Field completion_date = new Field("completion_date", "text", "Completion Date");
        return new Field[]{checklisttemplate_id, completion_date};
    }

    private SirenLink[] produceLinks(ChecklistTemplate checklistTemplate) {
        SirenLink self = new SirenLink(new String[]{"self"}, String.format("/checklisttemplates/%s", checklistTemplate.getId()));
        return new SirenLink[]{self};
    }
}