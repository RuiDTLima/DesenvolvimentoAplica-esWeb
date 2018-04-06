package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import org.springframework.data.domain.Page;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Collection;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.CollectionLink;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Data;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Item;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OutChecklistItems {
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public OutChecklistItems(Page<ChecklistItem> checklistItems, int checklistId){
        List<Item> items =
                StreamSupport.stream(checklistItems.spliterator(), false)
                        .map(this::itemFromChecklistItem)
                        .collect(Collectors.toList());

        String pageHref = "/checklists";
        collection = new Collection(
                "1.0",
                "/checklists/" + checklistId + "/checklistitems?page=" + checklistItems.getNumber(),
                items.toArray(new Item[items.size()]),
                CollectionLink.produceLinks(checklistItems, pageHref),
                null);
    }

    private Item itemFromChecklistItem(ChecklistItem checklistItem){
        return new Item(
                "/checklists/" + checklistItem.getChecklist().getId() + "/checklistitems/" + checklistItem.getId(),
                dataFromChecklistItem(checklistItem),
                produceItemLinks(checklistItem));
    }

    private Data[] dataFromChecklistItem(ChecklistItem checklistItem){
        return new Data[]{
                new Data("checklistitem_id", Integer.toString(checklistItem.getId()), "Checklist Item Id"),
                new Data("name", checklistItem.getName(), "Name")
        };
    }

    private CollectionLink[] produceItemLinks(ChecklistItem checklistItem){
        List<CollectionLink> collectionLinks = new LinkedList<>();
        collectionLinks.add(new CollectionLink("checklist", "/checklists/" + checklistItem.getChecklist().getId()));
        return collectionLinks.toArray(new CollectionLink[collectionLinks.size()]);
    }
}