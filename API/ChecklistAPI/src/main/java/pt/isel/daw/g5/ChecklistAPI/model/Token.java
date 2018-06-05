package pt.isel.daw.g5.ChecklistAPI.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Token {

    private boolean active;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user_id;

    public Token(boolean active, String user_id) {
        this.active = active;
        this.user_id = user_id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
