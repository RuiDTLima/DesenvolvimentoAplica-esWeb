package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;

public class DatabaseTemplateItem {
    private int templateitem_id;
    private String name;
    private String description;
    private int checklisttemplate_id;

    public int getTemplateitem_id() {
        return templateitem_id;
    }

    public void setTemplateitem_id(int templateitem_id) {
        this.templateitem_id = templateitem_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChecklisttemplate_id() {
        return checklisttemplate_id;
    }

    public void setChecklisttemplate_id(int checklisttemplate_id) {
        this.checklisttemplate_id = checklisttemplate_id;
    }

    protected DatabaseTemplateItem(){

    }

    public DatabaseTemplateItem(TemplateItem templateItem){
        this.templateitem_id = templateItem.getId();
        this.name = templateItem.getName();
        this.description = templateItem.getDescription();
        this.checklisttemplate_id = templateItem.getChecklistTemplate().getId();
    }
}