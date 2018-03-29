package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeResource {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("href")
    private String href;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("href-template")
    private String href_template;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("href-vars")
    private HrefVars hrefVars;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("hints")
    private Hints hints;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

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

    public HomeResource(String href, Hints hints) {
        this.href = href;
        this.hints = hints;
    }

    public HomeResource(String href_template, HrefVars hrefVars, Hints hints) {
        this.href_template = href_template;
        this.hrefVars = hrefVars;
        this.hints = hints;
    }
}