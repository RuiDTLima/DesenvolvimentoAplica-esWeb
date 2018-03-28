package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonInclude;

public class InvalidParams {
    private String name;
    private String reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String given;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public InvalidParams(String name, String reason, String given) {
        this.name = name;
        this.reason = reason;
        this.given = given;
    }

    public InvalidParams(String name, String reason){
        this.name = name;
        this.reason = reason;
    }
}