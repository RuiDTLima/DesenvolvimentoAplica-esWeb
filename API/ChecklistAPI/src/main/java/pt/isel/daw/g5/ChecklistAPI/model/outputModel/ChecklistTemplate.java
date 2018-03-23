package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ChecklistTemplate {
    private String name;
    @JsonAlias("checklisttemplate_id")
    private int checklistTemplateId;

    public ChecklistTemplate(String name, int checklistTemplateId) {
        this.name = name;
        this.checklistTemplateId = checklistTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(int checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }
}
