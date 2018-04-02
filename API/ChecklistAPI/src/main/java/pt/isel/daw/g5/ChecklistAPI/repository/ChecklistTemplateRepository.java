package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;

public interface ChecklistTemplateRepository extends PagingAndSortingRepository<ChecklistTemplate, Integer> {

    Page<ChecklistTemplate> findAllByUser(User user, Pageable pageable);
}