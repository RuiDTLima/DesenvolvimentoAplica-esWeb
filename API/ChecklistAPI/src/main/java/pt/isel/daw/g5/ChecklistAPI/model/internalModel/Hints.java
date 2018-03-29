package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hints {
    private String[] allow;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] representations;

    @JsonProperty("accept-post")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] accept_post;

    @JsonProperty("accept-put")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] accept_put;

    public String[] getAllow() {
        return allow;
    }

    public void setAllow(String[] allow) {
        this.allow = allow;
    }

    public String[] getRepresentations() {
        return representations;
    }

    public void setRepresentations(String[] representations) {
        this.representations = representations;
    }

    public String[] getAccept_post() {
        return accept_post;
    }

    public void setAccept_post(String[] accept_post) {
        this.accept_post = accept_post;
    }

    public String[] getAccept_put() {
        return accept_put;
    }

    public void setAccept_put(String[] accept_put) {
        this.accept_put = accept_put;
    }

    public Hints(String[] allow, String[] representations, String[] accept_post, String[] accept_put) {
        this.allow = allow;
        this.representations = representations;
        this.accept_post = accept_post;
        this.accept_put = accept_put;
    }
}
