package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import org.springframework.data.domain.Page;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.TemplateItem;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Collection;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.CollectionLink;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Data;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Item;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OutTemplateItems {
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public OutTemplateItems(Page<TemplateItem> templateItems, int checklistTemplateId){
        List<Item> items =
                StreamSupport.stream(templateItems.spliterator(), false)
                        .map(this::itemFromTemplateItem)
                        .collect(Collectors.toList());

        String pageHref = "/checklisttemplates";
        collection = new Collection(
                "1.0",
                "/checklisttemplates/" + checklistTemplateId + "/templateitems?page=" + (templateItems.getNumber()),
                items.toArray(new Item[items.size()]),
                CollectionLink.produceLinks(templateItems, pageHref));
    }

    private Item itemFromTemplateItem(TemplateItem templateItem){
        return new Item(
                "/checklisttemplates/" + templateItem.getChecklistTemplate().getId() + "/templateitems/" + templateItem.getId(),
                dataFromTemplateItem(templateItem),
                produceItemLinks(templateItem));
    }

    private Data[] dataFromTemplateItem(TemplateItem templateItem){
        return new Data[]{
                new Data("templateitem_id", Integer.toString(templateItem.getId()), "Template Item Id"),
                new Data("name", templateItem.getName(), "Name")
        };
    }

    private CollectionLink[] produceItemLinks(TemplateItem templateItem){
        List<CollectionLink> collectionLinks = new LinkedList<>();
        collectionLinks.add(new CollectionLink("checklisttemplate", "/checklisttemplates/" + templateItem.getChecklistTemplate().getId()));
        return collectionLinks.toArray(new CollectionLink[collectionLinks.size()]);
    }
}