package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;

public interface ChecklistRepository extends PagingAndSortingRepository<Checklist, Integer> {
    Page<Checklist> findAllByUsername(User username, Pageable pageable);
}