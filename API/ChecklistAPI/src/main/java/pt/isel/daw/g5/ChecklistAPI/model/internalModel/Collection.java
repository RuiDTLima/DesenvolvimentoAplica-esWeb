package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Collection {
    private String version;
    private String href;
    private Item[] items;

    @JsonProperty("links")
    private CollectionLink[] links;

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

    public Collection(String version, String href, Item[] items, CollectionLink[] collectionLinks) {
        this.version = version;
        this.href = href;
        this.items = items;
        this.links = collectionLinks;
    }
}