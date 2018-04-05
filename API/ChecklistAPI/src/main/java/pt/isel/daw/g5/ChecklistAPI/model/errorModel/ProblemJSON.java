package pt.isel.daw.g5.ChecklistAPI.model.errorModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;

public class ProblemJSON {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("invalid-params")
    private InvalidParams[] invalid_params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public InvalidParams[] getInvalid_params() {
        return invalid_params;
    }

    public void setInvalid_params(InvalidParams[] invalid_params) {
        this.invalid_params = invalid_params;
    }

    public ProblemJSON(String type, String title, int status, String detail, String instance, InvalidParams[] invalid_params) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.invalid_params = invalid_params;
    }
}