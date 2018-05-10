package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.isel.daw.g5.ChecklistAPI.model.databaseModels.DatabaseUser;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Action;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Entity;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.SirenLink;


public class OutUser {

    @JsonProperty("class")
    private String[] _class;

    @JsonProperty("properties")
    private DatabaseUser properties;

    @JsonProperty("entities")
    private Entity[] entities;

    @JsonProperty("actions")
    private Action[] actions;

    @JsonProperty("links")
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public DatabaseUser getProperties() {
        return properties;
    }

    public void setProperties(DatabaseUser properties) {
        this.properties = properties;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public SirenLink[] getSirenLinks() {
        return sirenLinks;
    }

    public void setSirenLinks(SirenLink[] sirenLinks) {
        this.sirenLinks = sirenLinks;
    }

    public OutUser(DatabaseUser user){
        _class = new String[]{"user"};
        properties = user;
    }
}
