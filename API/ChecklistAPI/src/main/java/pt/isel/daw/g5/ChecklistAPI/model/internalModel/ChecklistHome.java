package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChecklistHome {
    @JsonProperty("href-template")
    private String href_template;

    @JsonProperty("href-vars")
    private HrefVars hrefVars;

    @JsonProperty("hints")
    private Hints hints;

    public String getHref_template() {
        return href_template;
    }

    public void setHref_template(String href_template) {
        this.href_template = href_template;
    }

    public HrefVars getHrefVars() {
        return hrefVars;
    }

    public void setHrefVars(HrefVars hrefVars) {
        this.hrefVars = hrefVars;
    }

    public Hints getHints() {
        return hints;
    }

    public void setHints(Hints hints) {
        this.hints = hints;
    }

    public ChecklistHome(String href_template, HrefVars hrefVars, Hints hints) {
        this.href_template = href_template;
        this.hrefVars = hrefVars;
        this.hints = hints;
    }
}
