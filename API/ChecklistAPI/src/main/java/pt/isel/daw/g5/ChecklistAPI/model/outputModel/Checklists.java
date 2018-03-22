package pt.isel.daw.g5.ChecklistAPI.model.outputModel;

import pt.isel.daw.g5.ChecklistAPI.model.Items;
import pt.isel.daw.g5.ChecklistAPI.model.Link;

public class Checklists {
    private String version;
    private String href;
    private Items[] items;
    private Link[] links;

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

    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }

    public Link[] getLinks() {
        return links;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

    public Checklists(String version, String href, Items[] items, Link[] links) {
        this.version = version;
        this.href = href;
        this.items = items;
        this.links = links;
    }
}