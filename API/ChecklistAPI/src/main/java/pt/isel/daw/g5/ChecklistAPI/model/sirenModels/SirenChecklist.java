package pt.isel.daw.g5.ChecklistAPI.model.sirenModels;

public class SirenChecklist {
    private int id;
    private String name;
    private String completionDate;
    private String checklistTemplateId;

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

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(String checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }

    public SirenChecklist(int id, String name, String completionDate, String checklistTemplateId) {
        this.id = id;
        this.name = name;
        this.completionDate = completionDate;
        this.checklistTemplateId = checklistTemplateId;
    }
}
