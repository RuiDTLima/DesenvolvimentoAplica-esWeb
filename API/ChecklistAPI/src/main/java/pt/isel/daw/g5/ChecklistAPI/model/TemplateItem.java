package pt.isel.daw.g5.ChecklistAPI.model;

import javax.persistence.*;

@Entity
public class TemplateItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @ManyToOne
    private int checklistTemplateId;

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

    public int getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(int checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }

    protected TemplateItem(){

    }

    public TemplateItem(String name, String description, int checklistTemplateId) {
        this.name = name;
        this.description = description;
        this.checklistTemplateId = checklistTemplateId;
    }
}