package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import java.time.LocalDateTime;

public class DatabaseChecklist {
    private int id;
    private String name;
    private LocalDateTime completionDate;
    private String username;
    private int checklisttemplate_id;

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

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getChecklisttemplate_id() {
        return checklisttemplate_id;
    }

    public void setChecklisttemplate_id(int checklisttemplate_id) {
        this.checklisttemplate_id = checklisttemplate_id;
    }
}