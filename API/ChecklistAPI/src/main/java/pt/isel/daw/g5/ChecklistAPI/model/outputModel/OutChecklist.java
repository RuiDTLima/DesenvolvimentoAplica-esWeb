package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class OutChecklist {
    @JsonAlias("class")
    private String[] _class;

    private Checklist properties;
    private Entity[] entities;
    private Action[] actions;
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public Checklist getProperties() {
        return properties;
    }

    public void setProperties(Checklist properties) {
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

    public OutChecklist(Checklist checklist) {
        _class = new String[] {"checklist"};
        properties = checklist;
        entities = produceEntities(checklist);
        actions = produceActions(checklist);
        sirenLinks = produceLinks(checklist);
    }

    private Entity[] produceEntities(Checklist checklist) {
        return new Entity[] {produceChecklistItemEntity(checklist), produceChecklistTemplateEntity(checklist)};
    }

    private Entity produceChecklistItemEntity(Checklist checklist) {
        return new Entity(new String[] {"checklistitem"},  new String[] {"/checklists/" + checklist.getId()}, "/checklists/" + checklist.getId() + "/checklistitems"
                // TODO LINKS IN SIREN -> new SirenLink[0]
        );
    }

    private Entity produceChecklistTemplateEntity(Checklist checklist) {
        return new Entity(
                new String[] {"checklisttemplates"},
                new String[] {"/checklisttemplates"},
                ""
                // TODO LINKS IN SIREN -> new SirenLink[] { new SirenLink("self", "/checklisttemplates/" + checklist.getId()) }
        );
    }

    private Action[] produceActions(Checklist checklist) {
        return new Action[]{
                produceAddChecklistAction(checklist),
                produceDeleteChecklistAction(checklist),
                produceUpdateChecklistAction(checklist)
        };
    }

    private Action produceAddChecklistAction(Checklist checklist) {
        return new Action(
                "add-checklistitem",
                "Add Checklist Item",
                "POST",
                "/checklists/" + checklist.getId() + "/checklistitems",
                "application/json",
                new Field[] {
                        new Field("checklist_id", "hidden", "1"),
                        new Field("name", "text", ""),
                        new Field("description", "text", "")
                });
    }

    private Action produceDeleteChecklistAction(Checklist checklist) {
        return new Action(
                "delete-checklist",
                "Delete Checklist",
                "DELETE",
                "/checklists/" + checklist.getId(),
                "application/x-www-form-urlencoded",
                new Field[] {
                        new Field("checklist_id", "hidden", Integer.toString(checklist.getId()))
                }
        );
    }

    private Action produceUpdateChecklistAction(Checklist checklist) {
        return new Action(
                "update-checklist",
                "Update Checklist",
                "PUT",
                "/checklists/" + checklist.getId(),
                "application/json",
                new Field[] {
                        new Field("checklist_id", "hidden", "" + checklist.getId()),
                        new Field("name", "text", ""),
                        new Field("completion_date", "text", "")
                }
        );
    }

    private SirenLink[] produceLinks(Checklist checklist) {
        SirenLink self = new SirenLink(new String[] {"self"}, "/checklists" + checklist.getId());
        // TODO NEXT AND PREV
        return new SirenLink[]{self};
    }
}