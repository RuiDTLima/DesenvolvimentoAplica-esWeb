package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonAlias;

public class TemplateItem {
    private String name;
    @JsonAlias("templateitem_id")
    private int templateItemId;
    private String description;

    public TemplateItem(String name, int templateItemId, String description) {
        this.name = name;
        this.templateItemId = templateItemId;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemplateItemId() {
        return templateItemId;
    }

    public void setTemplateItemId(int templateItemId) {
        this.templateItemId = templateItemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
