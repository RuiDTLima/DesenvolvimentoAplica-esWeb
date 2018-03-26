package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;

public class DatabaseChecklistTemplate {
    private int id;
    private String name;
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DatabaseChecklistTemplate(ChecklistTemplate checklistTemplate) {
        id = checklistTemplate.getId();
        name = checklistTemplate.getName();
        username = checklistTemplate.getUserName().getUsername();
    }
}