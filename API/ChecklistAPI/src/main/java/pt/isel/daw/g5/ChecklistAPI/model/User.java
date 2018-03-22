package pt.isel.daw.g5.ChecklistAPI.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String userName;

    private String password;

    private User(){}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
