package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import org.springframework.data.domain.Page;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.ChecklistTemplate;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OutChecklistTemplates {
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public OutChecklistTemplates(Page<ChecklistTemplate> checklistTemplates){
        List<Item> items =
                StreamSupport.stream(checklistTemplates.spliterator(), false)
                        .map(this::itemFromChecklistTemplate)
                        .collect(Collectors.toList());

        String pageHref = "/checklisttemplates";
        collection = new Collection(
                "1.0",
                "/checklisttemplates?page=" + checklistTemplates.getNumber(),
                items.toArray(new Item[items.size()]),
                CollectionLink.produceLinks(checklistTemplates, pageHref),
                produceTemplate());
    }

    private Item itemFromChecklistTemplate(ChecklistTemplate checklistTemplate){
        return new Item(
                "/checklisttemplates/" + checklistTemplate.getId(),
                dataFromChecklistTemplate(checklistTemplate),
                produceItemLinks(checklistTemplate));
    }

    private Data[] dataFromChecklistTemplate(ChecklistTemplate checklistTemplate){
        return new Data[]{
                new Data("checklisttemplate_id", Integer.toString(checklistTemplate.getId()), "Checklist Template Id"),
                new Data("name", checklistTemplate.getName(), "Name")
        };
    }

    private CollectionLink[] produceItemLinks(ChecklistTemplate checklistTemplate){
        List<CollectionLink> collectionLinks = new LinkedList<>();
        collectionLinks.add(new CollectionLink("templateitems", "/checklisttemplates/" + checklistTemplate.getId() + "/templateitems"));
        return collectionLinks.toArray(new CollectionLink[collectionLinks.size()]);
    }

    private Template produceTemplate() {
        Data[] data = new Data[] {
                new Data("name", "", "Checklist Template Name")
        };
        return new Template(data);
    }
}
