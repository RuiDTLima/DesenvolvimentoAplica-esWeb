package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;
import pt.isel.daw.g5.ChecklistAPI.model.sirenModels.SirenChecklistItem;

public class OutChecklistItem {

    private Siren<SirenChecklistItem> siren;

    public Siren<SirenChecklistItem> getSiren() {
        return siren;
    }

    public void setSiren(Siren<SirenChecklistItem> siren) {
        this.siren = siren;
    }

    public OutChecklistItem(ChecklistItem checklistItem) {
        //ChecklistItem checklistItem = optionalChecklistItem.get();
        siren = new Siren<>(new String[]{"checklistitem"}, new SirenChecklistItem(checklistItem), produceEntity(checklistItem), produceActions(checklistItem), produceLinks(checklistItem));
    }

    private Entity[] produceEntity(ChecklistItem checklistItem) {
        int checklistId = checklistItem.getChecklistId().getId();
        String[] _class = new String[]{"checklist"};
        String[] rel = new String[]{"/checklists/" + checklistId};
        String href = String.format("/checklists/%s/checklistitems", checklistId);
        return new Entity[]{new Entity(_class, rel, href)};
    }

    private Action[] produceActions(ChecklistItem checklistItem) {
        return new Action[]{produceDelete(checklistItem), producePut(checklistItem)};
    }

    private Action produceDelete(ChecklistItem checklistItem) {
        return new Action("delete-checklistitem", "Delete OutChecklist Item", "DELETE", String.format("/checklists/%s/checklistitems/%s", checklistItem.getChecklistId().getId(), checklistItem.getId()), "application/x-www-form-urlencoded", produceDeleteFields(checklistItem));
    }

    private Action producePut(ChecklistItem checklistItem) {
        return new Action("update-checklistitem", "Update OutChecklist Item", "PUT", String.format("/checklists/%s/checklistitems/%s", checklistItem.getChecklistId().getId(), checklistItem.getId()), "application/json", producePutFields(checklistItem));
    }

    private Field[] produceDeleteFields(ChecklistItem checklistItem) {
        Field checklist_id = new Field("checklist_id", "hidden", Integer.toString(checklistItem.getChecklistId().getId()), "OutChecklist Id");
        Field checklistitem_id = new Field("checklistitem_id", "hidden", Integer.toString(checklistItem.getId()), "OutChecklist Item Id");
        return new Field[]{checklist_id, checklistitem_id};
    }

    private Field[] producePutFields(ChecklistItem checklistItem) {
        Field checklist_id = new Field("checklist_id", "hidden", Integer.toString(checklistItem.getChecklistId().getId()), "OutChecklist Id");
        Field id = new Field("id", "hidden", Integer.toString(checklistItem.getId()), "OutChecklist Item Id");
        Field name = new Field("name", "text", "Name");
        Field description = new Field("description", "text", "Description");
        Field state = new Field("state", "text", "State");
        return new Field[]{checklist_id, id, name, description, state};
    }

    private SirenLink[] produceLinks(ChecklistItem checklistItem) {
        SirenLink self = new SirenLink(new String[]{"self"}, String.format("/checklists/%s/checklistitems/%s", checklistItem.getChecklistId().getId(), checklistItem.getId()));
        //TODO adicionar os links next e previous
        return new SirenLink[]{self};
    }
}