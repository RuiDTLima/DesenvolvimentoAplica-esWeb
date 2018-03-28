package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entity {
    @JsonProperty("class")
    private String[] _class;
    @JsonProperty("res")
    private String[] rel;
    @JsonProperty("href")
    private String href;

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

    public Entity(String[] _class, String[] rel, String href) {
        this._class = _class;
        this.rel = rel;
        this.href = href;
    }
}