package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Checklist {
    private String name;
    @JsonAlias("completion_date")
    private String completionDate;
    @JsonAlias("checklist_id")
    private int checklistId;
    @JsonAlias("checklisttemplate_id")
    private int checklistTemplateId;

    public Checklist(String name, String completionDate, int checklistId, int checklistTemplateId) {
        this.name = name;
        this.completionDate = completionDate;
        this.checklistId = checklistId;
        this.checklistTemplateId = checklistTemplateId;
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

    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }

    public int getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(int checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }
}
