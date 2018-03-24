package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;

@Entity
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String state;

    @ManyToOne
    private Checklist ChecklistId;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Checklist getChecklistId() {
        return ChecklistId;
    }

    public void setChecklistId(Checklist checklistId) {
        ChecklistId = checklistId;
    }

    protected ChecklistItem(){

    }

    public ChecklistItem(String name, String description, String state) {
        this.name = name;
        this.description = description;
        this.state = state;
    }
}
