package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;

public class DatabaseChecklistItem {
    private int id;
    private String name;
    private String description;
    private String state;
    private int checklist_id;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(int checklist_id) {
        this.checklist_id = checklist_id;
    }

    public DatabaseChecklistItem() {
    }

    public DatabaseChecklistItem(ChecklistItem checklistItem){
        id = checklistItem.getId();
        name = checklistItem.getName();
        description = checklistItem.getDescription();
        state = checklistItem.getState();
        checklist_id = checklistItem.getChecklistId().getId();
    }
}