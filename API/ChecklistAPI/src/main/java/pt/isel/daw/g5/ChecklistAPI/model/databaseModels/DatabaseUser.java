package pt.isel.daw.g5.ChecklistAPI.model.databaseModels;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;

public class DatabaseUser {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DatabaseUser(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}