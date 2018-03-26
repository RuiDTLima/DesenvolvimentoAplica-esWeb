package pt.isel.daw.g5.ChecklistAPI.model.sirenModels;

public class SirenTemplateItem {
    private int id;
    private String name;
    private String description;
    private String templateId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public SirenTemplateItem(int id, String name, String description, String templateId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.templateId = templateId;
    }
}
