package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class Collection {

    private String version;
    private String href;
    private Item[] items;
    private Link[] links;

    public Collection(String version, String href, Item[] items, Link[] links) {
        this.version = version;
        this.href = href;
        this.items = items;
        this.links = links;
    }

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

    public Link[] getLinks() {
        return links;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

}
