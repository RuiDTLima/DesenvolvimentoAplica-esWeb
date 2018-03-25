package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.repository.CrudRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;

public interface UserRepository extends CrudRepository<User, String> {
}