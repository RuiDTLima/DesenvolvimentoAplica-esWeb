package pt.isel.daw.g5.ChecklistAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;

public interface TemplateItemRepository extends PagingAndSortingRepository<TemplateItem, Integer> {
    Page<TemplateItem> findAllByChecklistTemplate_Id(int checklistTemplate, Pageable pageable);

    void deleteAllByChecklistTemplateId(int checklistTemplateId);
}