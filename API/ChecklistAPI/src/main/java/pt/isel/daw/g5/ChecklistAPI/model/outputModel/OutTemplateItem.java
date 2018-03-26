package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class OutTemplateItem {
    @JsonAlias("class")
    private String[] _class;
    private TemplateItem properties;
    private Entity[] entities;
    private Action[] actions;
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public TemplateItem getProperties() {
        return properties;
    }

    public void setProperties(TemplateItem properties) {
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

    public OutTemplateItem(TemplateItem templateItem){
        _class = new String[] {};
        properties = templateItem;
        entities = produceEntities(templateItem);
        actions = produceActions(templateItem);
        sirenLinks = produceLinks(templateItem);
    }

    private Entity[] produceEntities(TemplateItem templateItem) {
        return new Entity[]{
                produceChecklistTemplateEntity(templateItem)
        };
    }

    private Entity produceChecklistTemplateEntity(TemplateItem templateItem) {
        return new Entity(
                new String[] {"checklisttemplate"},
                new String[] {"/checklisttemplates/" + templateItem.getChecklistTemplate().getId()},
                "/checklisttemplates/" + templateItem.getChecklistTemplate().getId()
                // TODO LINKS IN SIREN -> new SirenLink[0]
        );
    }

    private Action[] produceActions(TemplateItem templateItem) {
        return new Action[]{
                produceDeleteTemplateItemAction(templateItem),
                produceUpdateTemplateItemAction(templateItem)
        };
    }

    private Action produceDeleteTemplateItemAction(TemplateItem templateItem) {
        return new Action(
                "delete-templateitem",
                "Delete Template Item",
                "DELETE",
                "/checklisttemplates/" + templateItem.getChecklistTemplate().getId() + "/templateitems/" + templateItem.getId(),
                "application/x-www-form-urlencoded",
                new Field[] {
                        new Field("checklisttemplate_id", "hidden", Integer.toString(templateItem.getChecklistTemplate().getId())),
                        new Field("templateitem_id", "hidden", Integer.toString(templateItem.getId()))
                }
        );
    }

    private Action produceUpdateTemplateItemAction(TemplateItem templateItem) {
        return new Action(
                "update-templateitem",
                "Update Template Item",
                "PUT",
                "/checklisttemplates/" + templateItem.getChecklistTemplate().getId() + "/templateitems/" + templateItem.getId(),
                "application/json",
                new Field[] {
                        new Field("checklisttemplate_id", "hidden", "" + templateItem.getChecklistTemplate().getId()),
                        new Field("templateitem_id", "hidden", Integer.toString(templateItem.getId())),
                        new Field("name", "text", ""),
                        new Field("description", "text", "")
                }
        );
    }

    private SirenLink[] produceLinks(TemplateItem templateItem) {
        SirenLink self = new SirenLink(new String[]{"self"}, "/checklisttemplates/" + templateItem.getChecklistTemplate().getId() + "/templateitems/" + templateItem.getId());
        // TODO NEXT AND PREV
        return new SirenLink[]{self};
    }
}