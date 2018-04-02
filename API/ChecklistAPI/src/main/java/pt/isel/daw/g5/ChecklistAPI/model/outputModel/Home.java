package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

public class Home {
    private Resources resources;

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Home() {
        HomeResource checklistsHome = new HomeResource("/checklists", produceChecklistsHints());
        HomeResource checklistHome = new HomeResource("/checklists/{checklist_id}", produceChecklistHrefVars(), produceChecklistHints());
        HomeResource checklistTemplatesHome = new HomeResource("/checklisttemplates", produceChecklistTemplatesHints());
        HomeResource checklistTemplateHome = new HomeResource("/checklisttemplates/{checklisttemplate_id}", produceChecklistTemplateHrefVars(), produceChecklistTemplateHints());
        HomeResource usersHome = new HomeResource("/users", produceUsersHints());
        resources = new Resources(checklistsHome, checklistHome, checklistTemplatesHome, checklistTemplateHome, usersHome);
    }

    private Hints produceChecklistsHints() {
        return new Hints(new String[]{"GET", "POST"}, new String[]{"application/vnd.collection+json"}, new String[]{"application/json"}, null);
    }

    private HrefVars produceChecklistHrefVars() {
        return new HrefVars("/checklists", null);
    }

    private Hints produceChecklistHints() {
        return new Hints(new String[]{"GET", "PUT", "DELETE"}, new String[]{"application/vnd.siren+json"}, null, new String[]{"application/json"});
    }

    private Hints produceChecklistTemplatesHints() {
        return new Hints(new String[]{"GET", "POST"}, new String[]{"application/vnd.collection+json"}, new String[]{"application/json"}, null);
    }

    private HrefVars produceChecklistTemplateHrefVars() {
        return new HrefVars(null, "/checklisttemplates");
    }

    private Hints produceChecklistTemplateHints() {
        return new Hints(new String[]{"GET", "PUT", "DELETE"}, new String[]{"application/vnd.siren+json"}, null, new String[]{"application/json"});
    }

    private Hints produceUsersHints() {
        return new Hints(new String[]{"POST"}, null, new String[]{"application/json"}, null);
    }
}