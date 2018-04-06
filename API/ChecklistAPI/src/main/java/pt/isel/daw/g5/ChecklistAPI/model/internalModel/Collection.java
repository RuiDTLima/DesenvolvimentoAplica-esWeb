package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Collection {
    private String version;
    private String href;
    private Item[] items;

    @JsonProperty("links")
    private CollectionLink[] links;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Template template;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    @JsonProperty("links")
    public CollectionLink[] getCollectionLinks() {
        return links;
    }

    public void setCollectionLinks(CollectionLink[] collectionLinks) {
        this.links = collectionLinks;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Collection(String version, String href, Item[] items, CollectionLink[] collectionLinks, Template template) {
        this.version = version;
        this.href = href;
        this.items = items;
        this.links = collectionLinks;
        this.template = template;
    }
}