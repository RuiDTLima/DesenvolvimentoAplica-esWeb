package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;

public class DatabaseChecklistTemplate {
    @JsonProperty(value = "checklisttemplate_id")
    private int id;
    private String name;
    private boolean usable;

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

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    protected DatabaseChecklistTemplate(){
    }

    public DatabaseChecklistTemplate(ChecklistTemplate checklistTemplate) {
        id = checklistTemplate.getId();
        name = checklistTemplate.getName();
        usable = checklistTemplate.isUsable();
    }
}