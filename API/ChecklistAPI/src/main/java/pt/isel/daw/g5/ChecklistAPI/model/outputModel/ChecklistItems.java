package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Collection;

import java.util.List;
import java.util.Optional;

public class ChecklistItems {

    private Optional<Checklist> checklists;
    private Collection collection;

    public ChecklistItems(List<ChecklistItem> checklists) {


    }
}
