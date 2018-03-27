package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;

public class DatabaseTemplateItem {
    private int id;
    private String name;
    private String description;
    private int checklistTemplateId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(int checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }

    public DatabaseTemplateItem(TemplateItem templateItem){
        this.id = templateItem.getId();
        this.name = templateItem.getName();
        this.description = templateItem.getDescription();
        this.checklistTemplateId = templateItem.getChecklistTemplate().getId();
    }
}
