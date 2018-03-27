package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;

@Entity
@Table(name = "Checklisttemplateitem")
public class TemplateItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "Checklisttemplate_id")
    private ChecklistTemplate checklistTemplate;

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

    public ChecklistTemplate getChecklistTemplate() {
        return checklistTemplate;
    }

    public void setChecklistTemplate(ChecklistTemplate checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
    }

    protected TemplateItem(){

    }

    public TemplateItem(String name, String description) {
        this.name = name;
        this.description = description;
    }
}