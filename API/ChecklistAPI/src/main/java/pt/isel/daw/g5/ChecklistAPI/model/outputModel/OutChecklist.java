package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class OutChecklist {
    @JsonProperty("class")
    private String[] _class;
    private DatabaseChecklist properties;
    private Entity[] entities;
    private Action[] actions;

    @JsonProperty("links")
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public DatabaseChecklist getProperties() {
        return properties;
    }

    public void setProperties(DatabaseChecklist properties) {
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

    public OutChecklist(DatabaseChecklist checklist) {
        _class = new String[] {"checklist"};
        properties = checklist;
        entities = produceEntities(checklist);
        actions = produceActions(checklist);
        sirenLinks = produceLinks(checklist);
    }

    private Entity[] produceEntities(DatabaseChecklist checklist) {
        if (checklist.getChecklistTemplateId() != 0)
            return new Entity[] {produceChecklistItemEntity(checklist), produceChecklistTemplateEntity(checklist)};
        return new Entity[] {produceChecklistItemEntity(checklist)};
    }

    private Entity produceChecklistItemEntity(DatabaseChecklist checklist) {
        return new Entity(new String[] {"checklistitem"},  new String[] {"/checklists/" + checklist.getChecklistId()}, "/checklists/" + checklist.getChecklistId() + "/checklistitems");
    }

    private Entity produceChecklistTemplateEntity(DatabaseChecklist checklist) {
        return new Entity(
                new String[] {"checklisttemplates"},
                new String[] {"/checklisttemplates"},
                String.format("/checklisttemplates/%s", checklist.getChecklistTemplateId())
        );
    }

    private Action[] produceActions(DatabaseChecklist checklist) {
        return new Action[]{
                produceAddChecklistAction(checklist),
                produceDeleteChecklistAction(checklist),
                produceUpdateChecklistAction(checklist)
        };
    }

    private Action produceAddChecklistAction(DatabaseChecklist checklist) {
        return new Action(
                "add-checklistitem",
                "Add Checklist Item",
                "POST",
                "/checklists/" + checklist.getChecklistId() + "/checklistitems",
                "application/json",
                new Field[] {
                        new Field("checklist_id", "hidden", "1", "Checklist Id"),
                        new Field("name", "text", "Name"),
                        new Field("description", "text", "Description")
                });
    }

    private Action produceDeleteChecklistAction(DatabaseChecklist checklist) {
        return new Action(
                "delete-checklist",
                "Delete Checklist",
                "DELETE",
                "/checklists/" + checklist.getChecklistId()
        );
    }

    private Action produceUpdateChecklistAction(DatabaseChecklist checklist) {
        return new Action(
                "update-checklist",
                "Update Checklist",
                "PUT",
                "/checklists/" + checklist.getChecklistId(),
                "application/json",
                new Field[] {
                        new Field("checklist_id", "hidden", "" + checklist.getChecklistId(), "Checklist Id"),
                        new Field("name", "text", "Name"),
                        new Field("completion_date", "text", "Completion Date")
                }
        );
    }

    private SirenLink[] produceLinks(DatabaseChecklist checklist) {
        SirenLink self = new SirenLink(new String[] {"self"}, "/checklists/" + checklist.getChecklistId());
        return new SirenLink[]{self};
    }
}