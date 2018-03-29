package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HrefVars {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String checklist_id;

    @JsonProperty("checklisttemplate_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String checklistTemplate_id;

    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public String getChecklistTemplate_id() {
        return checklistTemplate_id;
    }

    public void setChecklistTemplate_id(String checklistTemplate_id) {
        this.checklistTemplate_id = checklistTemplate_id;
    }

    public HrefVars(String checklist_id, String checklistTemplate_id) {
        this.checklist_id = checklist_id;
        this.checklistTemplate_id = checklistTemplate_id;
    }
}