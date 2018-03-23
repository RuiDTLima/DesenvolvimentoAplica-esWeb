package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {

    Page<Checklist> findAll(Pageable pageable);
}