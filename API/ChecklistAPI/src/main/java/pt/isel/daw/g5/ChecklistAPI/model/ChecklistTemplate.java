package pt.isel.daw.g5.ChecklistAPI.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChecklistTemplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    private String userName;

    @OneToMany
    private List<TemplateItem> templateItems;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<TemplateItem> getTemplateItems() {
        return templateItems;
    }

    public void setTemplateItems(List<TemplateItem> templateItems) {
        this.templateItems = templateItems;
    }

    protected ChecklistTemplate(){

    }

    public ChecklistTemplate(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }
}