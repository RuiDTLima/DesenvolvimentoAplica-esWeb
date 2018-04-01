package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseChecklist;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class OutChecklist {
    @JsonAlias("class")
    private String[] _class;

    private DatabaseChecklist properties;
    private Entity[] entities;
    private Action[] actions;
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
        return new Entity[] {produceChecklistItemEntity(checklist), produceChecklistTemplateEntity(checklist)};
    }

    private Entity produceChecklistItemEntity(DatabaseChecklist checklist) {
        return new Entity(new String[] {"checklistitem"},  new String[] {"/checklists/" + checklist.getChecklist_id()}, "/checklists/" + checklist.getChecklist_id() + "/checklistitems"
                // TODO LINKS IN SIREN -> new SirenLink[0]
        );
    }

    private Entity produceChecklistTemplateEntity(DatabaseChecklist checklist) {
        return new Entity(
                new String[] {"checklisttemplates"},
                new String[] {"/checklisttemplates"},
                String.format("/checklisttemplates/%s", checklist.getChecklisttemplate_id())
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
                "/checklists/" + checklist.getChecklist_id() + "/checklistitems",
                "application/json",
                new Field[] {
                        new Field("checklist_id", "hidden", "1"),
                        new Field("name", "text", ""),
                        new Field("description", "text", "")
                });
    }

    private Action produceDeleteChecklistAction(DatabaseChecklist checklist) {
        return new Action(
                "delete-checklist",
                "Delete Checklist",
                "DELETE",
                "/checklists/" + checklist.getChecklist_id(),
                "application/x-www-form-urlencoded",
                new Field[] {
                        new Field("checklist_id", "hidden", Integer.toString(checklist.getChecklist_id()))
                }
        );
    }

    private Action produceUpdateChecklistAction(DatabaseChecklist checklist) {
        return new Action(
                "update-checklist",
                "Update Checklist",
                "PUT",
                "/checklists/" + checklist.getChecklist_id(),
                "application/json",
                new Field[] {
                        new Field("checklist_id", "hidden", "" + checklist.getChecklist_id()),
                        new Field("name", "text", ""),
                        new Field("completion_date", "text", "")
                }
        );
    }

    private SirenLink[] produceLinks(DatabaseChecklist checklist) {
        SirenLink self = new SirenLink(new String[] {"self"}, "/checklists" + checklist.getChecklist_id());
        return new SirenLink[]{self};
    }
}