package pt.isel.daw.g5.ChecklistAPI.model.internalModel;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Resources {
    @JsonProperty("/checklists")
    private HomeResource checklistsHome;

    @JsonProperty("/checklist")
    private HomeResource checklistHome;

    @JsonProperty("/checklisttemplates")
    private HomeResource checklistTemplatesHome;

    @JsonProperty("/checklisttemplate")
    private HomeResource checklistTemplateHome;

    @JsonProperty("/users")
    private HomeResource usersHome;

    public HomeResource getChecklistsHome() {
        return checklistsHome;
    }

    public void setChecklistsHome(HomeResource checklistsHome) {
        this.checklistsHome = checklistsHome;
    }

    public HomeResource getChecklistHome() {
        return checklistHome;
    }

    public void setChecklistHome(HomeResource checklistHome) {
        this.checklistHome = checklistHome;
    }

    public HomeResource getChecklistTemplatesHome() {
        return checklistTemplatesHome;
    }

    public void setChecklistTemplatesHome(HomeResource checklistTemplatesHome) {
        this.checklistTemplatesHome = checklistTemplatesHome;
    }

    public HomeResource getChecklistTemplateHome() {
        return checklistTemplateHome;
    }

    public void setChecklistTemplateHome(HomeResource checklistTemplateHome) {
        this.checklistTemplateHome = checklistTemplateHome;
    }

    public HomeResource getUsersHome() {
        return usersHome;
    }

    public void setUsersHome(HomeResource usersHome) {
        this.usersHome = usersHome;
    }

    public Resources(HomeResource checklistsHome,
                     HomeResource checklistHome,
                     HomeResource checklistTemplatesHome,
                     HomeResource checklistTemplateHome,
                     HomeResource usersHome) {

        this.checklistsHome = checklistsHome;
        this.checklistHome = checklistHome;
        this.checklistTemplatesHome = checklistTemplatesHome;
        this.checklistTemplateHome = checklistTemplateHome;
        this.usersHome = usersHome;
    }
}