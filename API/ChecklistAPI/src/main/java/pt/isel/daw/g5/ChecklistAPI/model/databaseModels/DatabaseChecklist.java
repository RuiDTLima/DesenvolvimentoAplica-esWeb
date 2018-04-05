package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import java.time.LocalDateTime;

public class DatabaseChecklist {
    @JsonAlias("checklistId")
    private int checklistId;

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonAlias("completion_date")
    private LocalDateTime completionDate;

    @JsonAlias("username")
    private String username;

    @JsonAlias("checklistTemplateId")
    private int checklistTemplateId;

    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
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

    public int getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(int checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }

    public DatabaseChecklist() {}

    public DatabaseChecklist(Checklist checklist){
        this.checklistId = checklist.getId();
        this.name = checklist.getName();
        this.completionDate = checklist.getCompletionDate();
        if(checklist.getChecklistTemplate() != null)
            this.checklistTemplateId = checklist.getChecklistTemplate().getId();
    }
}