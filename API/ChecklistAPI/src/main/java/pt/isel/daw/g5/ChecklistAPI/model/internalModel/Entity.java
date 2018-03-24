package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Entity {
    @JsonAlias("class")
    private String[] _class;
    private String[] rel;
    private String href;
    private Link[] links;

    public String[] get_class() {
        return _class;
    }

    public void set_class(String[] _class) {
        this._class = _class;
    }

    public String[] getRel() {
        return rel;
    }

    public void setRel(String[] rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Link[] getLinks() {
        return links;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

    public Entity(String[] _class, String[] rel, String href, Link[] links) {
        this._class = _class;
        this.rel = rel;
        this.href = href;
        this.links = links;
    }
}