package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import org.springframework.data.domain.Page;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Collection;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Data;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Item;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.Checklist;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.Link;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Checklists {
    private Collection collection;

    public Checklists(Page<Checklist> checklistPage){
        List<Item> items =
                StreamSupport.stream(checklistPage.spliterator(), false)
                .map(cl -> itemFromChecklist(cl))
                .collect(Collectors.toList());

        String pageHref = "/checklists";
        collection = new Collection(
                "1.0",
                "/checklists?page=" + (checklistPage.getNumber() + 1),
                items.toArray(new Item[items.size()]),
                Link.produceLinks(checklistPage, pageHref));
    }

    private Item itemFromChecklist(Checklist checklist){
        return new Item(
                "/checklists/" + checklist.getId(),
                dataFromChecklist(checklist),
                produceItemLinks(checklist));
    }

    private Link[] produceItemLinks(Checklist checklist){
        List<Link> links = new LinkedList<>();
        links.add(new Link("items", "/checklists/" + checklist.getId() + "/checklistitems"));

        if(checklist.getChecklistTemplate() != null) {
            links.add(new Link("template", "/checklisttemplates/" + checklist.getChecklistTemplate().getId()));
        }

        return links.toArray(new Link[links.size()]);
    }

    private Data[] dataFromChecklist(Checklist checklist){
        return new Data[]{
                new Data("name", checklist.getName(), "Name"),
                new Data("checklist_id", Integer.toString(checklist.getId()), "Checklist Id"),
                new Data("completion_date", checklist.getCompletionDate().toString(), "Completion Date"),
                new Data("checklisttemplate_id", checklist.getChecklistTemplate() == null? "" : Integer.toString(checklist.getChecklistTemplate().getId()), "Checklist Template Id  ")};
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}