package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklistItem;

public class OutChecklistItem {
    @JsonAlias({"class", "ola"})
    private String[] _class;
    private DatabaseChecklistItem properties;
    private Entity[] entities;
    private Action[] actions;
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public DatabaseChecklistItem getProperties() {
        return properties;
    }

    public void setProperties(DatabaseChecklistItem properties) {
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

    public OutChecklistItem(ChecklistItem checklistItem){
        _class = new String[]{"checklistitem"};
        properties = new DatabaseChecklistItem(checklistItem);
        entities = produceEntity(checklistItem);
        actions = produceActions(checklistItem);
        sirenLinks = produceLinks(checklistItem);
    }

    private Entity[] produceEntity(ChecklistItem checklistItem) {
        int checklistId = checklistItem.getChecklistId().getId();
        String[] _class = new String[]{"checklist"};
        String[] rel = new String[]{"/checklists/" + checklistId};
        String href = String.format("/checklists/%s/checklistitems", checklistId);
        return new Entity[]{new Entity(_class, rel, href)};
    }

    private Action[] produceActions(ChecklistItem checklistItem) {
        return new Action[]{produceDelete(checklistItem), producePut(checklistItem)};
    }

    private Action produceDelete(ChecklistItem checklistItem) {
        return new Action("delete-checklistitem", "Delete OutChecklist Item", "DELETE", String.format("/checklists/%s/checklistitems/%s", checklistItem.getChecklistId().getId(), checklistItem.getId()), "application/x-www-form-urlencoded", produceDeleteFields(checklistItem));
    }

    private Action producePut(ChecklistItem checklistItem) {
        return new Action("update-checklistitem", "Update OutChecklist Item", "PUT", String.format("/checklists/%s/checklistitems/%s", checklistItem.getChecklistId().getId(), checklistItem.getId()), "application/json", producePutFields(checklistItem));
    }

    private Field[] produceDeleteFields(ChecklistItem checklistItem) {
        Field checklist_id = new Field("checklist_id", "hidden", Integer.toString(checklistItem.getChecklistId().getId()), "OutChecklist Id");
        Field checklistitem_id = new Field("checklistitem_id", "hidden", Integer.toString(checklistItem.getId()), "OutChecklist Item Id");
        return new Field[]{checklist_id, checklistitem_id};
    }

    private Field[] producePutFields(ChecklistItem checklistItem) {
        Field checklist_id = new Field("checklist_id", "hidden", Integer.toString(checklistItem.getChecklistId().getId()), "OutChecklist Id");
        Field id = new Field("id", "hidden", Integer.toString(checklistItem.getId()), "OutChecklist Item Id");
        Field name = new Field("name", "text", "Name");
        Field description = new Field("description", "text", "Description");
        Field state = new Field("state", "text", "State");
        return new Field[]{checklist_id, id, name, description, state};
    }

    private SirenLink[] produceLinks(ChecklistItem checklistItem) {
        SirenLink self = new SirenLink(new String[]{"self"}, String.format("/checklists/%s/checklistitems/%s", checklistItem.getChecklistId().getId(), checklistItem.getId()));
        //TODO adicionar os links next e previous
        return new SirenLink[]{self};
    }
}