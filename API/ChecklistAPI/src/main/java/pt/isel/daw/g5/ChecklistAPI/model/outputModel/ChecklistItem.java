package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ChecklistItem {
    private String name;

    @JsonAlias("checklistitem_id")
    private int checklistItemId;

    private String description;
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecklistItemId() {
        return checklistItemId;
    }

    public void setChecklistItemId(int checklistItemId) {
        this.checklistItemId = checklistItemId;
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

    public ChecklistItem(String name, int checklistItemId, String description, String state) {
        this.name = name;
        this.checklistItemId = checklistItemId;
        this.description = description;
        this.state = state;
    }
}