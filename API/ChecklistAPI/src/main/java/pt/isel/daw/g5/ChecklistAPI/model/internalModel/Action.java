package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Action {
    private String name;
    private String title;
    private String method;
    private String href;
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Field[] fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public Action(String name, String title, String method, String href, String type, Field[] fields) {
        this.name = name;
        this.title = title;
        this.method = method;
        this.href = href;
        this.type = type;
        this.fields = fields;
    }
}