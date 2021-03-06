package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Checklisttemplate")
public class ChecklistTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean usable;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "checklistTemplate")
    private List<TemplateItem> templateItems;

    @OneToMany(mappedBy = "checklistTemplate")
    private List<Checklist> checklists;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TemplateItem> getTemplateItems() {
        return templateItems;
    }

    public void setTemplateItems(List<TemplateItem> templateItems) {
        this.templateItems = templateItems;
    }

    public List<Checklist> getChecklists() {
        return checklists;
    }

    public void setChecklists(List<Checklist> checklists) {
        this.checklists = checklists;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    protected ChecklistTemplate(){
    }

    public ChecklistTemplate(String name) {
        this.name = name;
        usable = true;
    }
}