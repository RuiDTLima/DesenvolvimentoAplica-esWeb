package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;

public interface TemplateItemRepository extends PagingAndSortingRepository<TemplateItem, Integer> {
}
