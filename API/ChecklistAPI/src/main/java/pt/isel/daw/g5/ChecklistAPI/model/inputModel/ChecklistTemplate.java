package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChecklistTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    private User userName;

    @OneToMany
    private List<TemplateItem> templateItems;

    @OneToMany
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

    public User getUserName() {
        return userName;
    }

    public void setUserName(User userName) {
        this.userName = userName;
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

    protected ChecklistTemplate(){
    }

    public ChecklistTemplate(String name) {
        this.name = name;
    }
}