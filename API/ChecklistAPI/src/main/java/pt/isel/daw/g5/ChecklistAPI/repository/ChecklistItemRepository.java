package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;

public interface ChecklistItemRepository extends PagingAndSortingRepository<ChecklistItem, Integer> {
    Page<ChecklistItem> findAllByChecklistId(int checklistId, Pageable pageable);
}