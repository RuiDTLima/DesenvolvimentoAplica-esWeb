package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;

public interface ChecklistTemplateRepository extends PagingAndSortingRepository<ChecklistTemplate, Integer> {
}