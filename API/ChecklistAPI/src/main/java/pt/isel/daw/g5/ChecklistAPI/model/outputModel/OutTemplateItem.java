package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseTemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class OutTemplateItem {
    @JsonProperty("class")
    private String[] _class;

    private DatabaseTemplateItem properties;
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

    public DatabaseTemplateItem getProperties() {
        return properties;
    }

    public void setProperties(DatabaseTemplateItem properties) {
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

    public OutTemplateItem(DatabaseTemplateItem templateItem){
        _class = new String[] {"templateitem"};
        properties = templateItem;
        entities = produceEntities(templateItem);
        actions = produceActions(templateItem);
        sirenLinks = produceLinks(templateItem);
    }

    private Entity[] produceEntities(DatabaseTemplateItem templateItem) {
        return new Entity[]{
                produceChecklistTemplateEntity(templateItem)
        };
    }

    private Entity produceChecklistTemplateEntity(DatabaseTemplateItem templateItem) {
        return new Entity(
                new String[] {"checklisttemplate"},
                new String[] {"/checklisttemplates/" + templateItem.getChecklistTemplateId()},
                "/checklisttemplates/" + templateItem.getChecklistTemplateId()
                // TODO LINKS IN SIREN -> new SirenLink[0]
        );
    }

    private Action[] produceActions(DatabaseTemplateItem templateItem) {
        return new Action[]{
                produceDeleteTemplateItemAction(templateItem),
                produceUpdateTemplateItemAction(templateItem)
        };
    }

    private Action produceDeleteTemplateItemAction(DatabaseTemplateItem templateItem) {
        return new Action(
                "delete-templateitem",
                "Delete Template Item",
                "DELETE",
                "/checklisttemplates/" + templateItem.getChecklistTemplateId() + "/templateitems/" + templateItem.getId(),
                "application/x-www-form-urlencoded",
                new Field[] {
                        new Field("checklisttemplate_id", "hidden", Integer.toString(templateItem.getChecklistTemplateId())),
                        new Field("templateitem_id", "hidden", Integer.toString(templateItem.getId()))
                }
        );
    }

    private Action produceUpdateTemplateItemAction(DatabaseTemplateItem templateItem) {
        return new Action(
                "update-templateitem",
                "Update Template Item",
                "PUT",
                "/checklisttemplates/" + templateItem.getChecklistTemplateId() + "/templateitems/" + templateItem.getId(),
                "application/json",
                new Field[] {
                        new Field("checklisttemplate_id", "hidden", "" + templateItem.getChecklistTemplateId()),
                        new Field("templateitem_id", "hidden", Integer.toString(templateItem.getId())),
                        new Field("name", "text", ""),
                        new Field("description", "text", "")
                }
        );
    }

    private SirenLink[] produceLinks(DatabaseTemplateItem templateItem) {
        SirenLink self = new SirenLink(new String[]{"self"}, "/checklisttemplates/" + templateItem.getChecklistTemplateId() + "/templateitems/" + templateItem.getId());
        // TODO NEXT AND PREV
        return new SirenLink[]{self};
    }
}