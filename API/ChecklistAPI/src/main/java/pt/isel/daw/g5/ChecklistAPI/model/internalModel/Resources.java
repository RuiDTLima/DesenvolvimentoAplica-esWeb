package pt.isel.daw.g5.ChecklistAPI.model.internalModel;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Resources {
    @JsonProperty("/checklists")
    private ChecklistsHome checklistsHome;

    @JsonProperty("/checklist")
    private ChecklistHome checklistHome;

    @JsonProperty("/checklisttemplates")
    private ChecklistTemplatesHome checklistTemplatesHome;

    @JsonProperty("/checklisttemplate")
    private ChecklistTemplateHome checklistTemplateHome;

    @JsonProperty("/users")
    private UsersHome usersHome;

    public ChecklistsHome getChecklistsHome() {
        return checklistsHome;
    }

    public void setChecklistsHome(ChecklistsHome checklistsHome) {
        this.checklistsHome = checklistsHome;
    }

    public ChecklistHome getChecklistHome() {
        return checklistHome;
    }

    public void setChecklistHome(ChecklistHome checklistHome) {
        this.checklistHome = checklistHome;
    }

    public ChecklistTemplatesHome getChecklistTemplatesHome() {
        return checklistTemplatesHome;
    }

    public void setChecklistTemplatesHome(ChecklistTemplatesHome checklistTemplatesHome) {
        this.checklistTemplatesHome = checklistTemplatesHome;
    }

    public ChecklistTemplateHome getChecklistTemplateHome() {
        return checklistTemplateHome;
    }

    public void setChecklistTemplateHome(ChecklistTemplateHome checklistTemplateHome) {
        this.checklistTemplateHome = checklistTemplateHome;
    }

    public UsersHome getUsersHome() {
        return usersHome;
    }

    public void setUsersHome(UsersHome usersHome) {
        this.usersHome = usersHome;
    }

    public Resources(ChecklistsHome checklistsHome,
                     ChecklistHome checklistHome,
                     ChecklistTemplatesHome checklistTemplatesHome,
                     ChecklistTemplateHome checklistTemplateHome,
                     UsersHome usersHome) {

        this.checklistsHome = checklistsHome;
        this.checklistHome = checklistHome;
        this.checklistTemplatesHome = checklistTemplatesHome;
        this.checklistTemplateHome = checklistTemplateHome;
        this.usersHome = usersHome;
    }
}