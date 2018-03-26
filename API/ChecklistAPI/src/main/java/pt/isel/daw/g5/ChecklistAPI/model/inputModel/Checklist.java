package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Checklist")
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "completiondate")
    private LocalDateTime completionDate;

    @ManyToOne
    @JoinColumn(name = "Username")
    private User username;

    @ManyToOne
    @JoinColumn(name = "checklisttemplate_id")
    private ChecklistTemplate checklistTemplate;

    @OneToMany(mappedBy = "ChecklistId")
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

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public ChecklistTemplate getChecklistTemplate() {
        return checklistTemplate;
    }

    public void setChecklistTemplate(ChecklistTemplate checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
    }

    public List<ChecklistItem> getInChecklistItems() {
        return checklistItems;
    }

    public void setInChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    protected Checklist(){
    }

    public Checklist(String name, LocalDateTime completionDate) {
        this.name = name;
        this.completionDate = completionDate;
    }
}