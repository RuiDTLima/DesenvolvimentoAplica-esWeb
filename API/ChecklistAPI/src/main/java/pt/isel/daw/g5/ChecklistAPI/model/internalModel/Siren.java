package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Siren<T> {
    @JsonAlias("class")
    private String[] _class;
    private T properties;
    private Entity[] entities;
    private Action[] actions;
    private SirenLink[] sirenLinks;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
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

    public SirenLink[] getCollectionLinks() {
        return sirenLinks;
    }

    public void setCollectionLinks(SirenLink[] collectionLinks) {
        this.sirenLinks = collectionLinks;
    }

    public Siren(String[] _class, T properties, Entity[] entities, Action[] actions, SirenLink[] sirenLinks) {
        this._class = _class;
        this.properties = properties;
        this.entities = entities;
        this.actions = actions;
        this.sirenLinks = sirenLinks;
    }
}
