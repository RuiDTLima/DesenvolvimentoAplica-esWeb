package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;

import java.time.LocalDateTime;

public class DatabaseChecklist {
    private int checklist_id;
    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime completionDate;
    private String username;
    private int checklisttemplate_id;

    public int getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(int checklist_id) {
        this.checklist_id = checklist_id;
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

    public DatabaseChecklist() {}

    public DatabaseChecklist(Checklist checklist){
        this.checklist_id = checklist.getId();
        this.name = checklist.getName();
        this.completionDate = checklist.getCompletionDate();
        if(checklist.getChecklistTemplate() != null)
            this.checklisttemplate_id = checklist.getChecklistTemplate().getId();
    }
}