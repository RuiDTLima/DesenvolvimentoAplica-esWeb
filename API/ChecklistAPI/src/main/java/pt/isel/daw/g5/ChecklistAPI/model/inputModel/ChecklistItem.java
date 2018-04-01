package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;

@Entity
@Table(name = "Checklistitem")
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String state;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

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

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    protected ChecklistItem(){

    }

    public ChecklistItem(String name, String description, String state) {
        this.name = name;
        this.description = description;
        this.state = state;
    }
}
