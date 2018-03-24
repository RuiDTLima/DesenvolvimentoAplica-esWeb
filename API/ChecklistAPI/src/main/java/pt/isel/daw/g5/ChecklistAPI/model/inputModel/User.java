package pt.isel.daw.g5.ChecklistAPI.model.inputModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany
    private List<Checklist> checklists;

    @OneToMany
    private List<ChecklistTemplate> checklistTemplates;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Checklist> getChecklists() {
        return checklists;
    }

    public void setChecklists(List<Checklist> checklists) {
        this.checklists = checklists;
    }

    public List<ChecklistTemplate> getChecklistTemplates() {
        return checklistTemplates;
    }

    public void setChecklistTemplates(List<ChecklistTemplate> checklistTemplates) {
        this.checklistTemplates = checklistTemplates;
    }

    protected User(){
    }

    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }
}
