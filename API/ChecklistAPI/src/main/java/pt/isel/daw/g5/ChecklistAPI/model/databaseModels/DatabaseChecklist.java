package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import java.time.ZonedDateTime;

public class DatabaseChecklist {
    @JsonProperty("checklist_id")
    private int checklistId;

    private String name;

    @JsonProperty("completion_date")
    private String completionDate;

    @JsonProperty("checklisttemplate_id")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int checklistTemplateId;

    // só para output, indica se todos items dessa checklist estão completed
    @JsonProperty("completion_state")
    private String completionState;

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

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public int getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(int checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }

    public String getCompletionState() {
        return completionState;
    }

    public void setCompletionState(String completionState) {
        this.completionState = completionState;
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