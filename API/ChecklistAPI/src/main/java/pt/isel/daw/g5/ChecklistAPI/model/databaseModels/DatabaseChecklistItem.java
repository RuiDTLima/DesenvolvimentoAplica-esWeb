package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;

public class DatabaseChecklistItem {
    private int id;
    private String name;
    private String description;
    private String state;

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

    public DatabaseChecklistItem(ChecklistItem checklistItem){
        id = checklistItem.getId();
        name = checklistItem.getName();
        description = checklistItem.getDescription();
        state = checklistItem.getState();
    }
}