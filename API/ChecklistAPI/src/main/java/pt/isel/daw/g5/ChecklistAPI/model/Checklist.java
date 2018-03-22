package pt.isel.daw.g5.ChecklistAPI.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Checklist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String completionDate;

    @ManyToOne
    private String userName;

    @ManyToOne
    private int checklistTemplate;

    @OneToMany
    private List<ChecklistItem> checklistItems;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getChecklistTemplate() {
        return checklistTemplate;
    }

    public void setChecklistTemplate(int checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    protected Checklist(){
    }

    public Checklist(String name, String completionDate, String userName, int checklistTemplate) {
        this.name = name;
        this.completionDate = completionDate;
        this.userName = userName;
        this.checklistTemplate = checklistTemplate;
    }
}