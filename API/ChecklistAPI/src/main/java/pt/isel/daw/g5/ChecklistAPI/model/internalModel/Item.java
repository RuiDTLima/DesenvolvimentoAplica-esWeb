package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class Item {
    private String href;
    private Data[] data;
    private Link[] links;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Link[] getLinks() {
        return links;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

    public Item(String href, Data[] data, Link[] links) {
        this.href = href;
        this.data = data;
        this.links = links;
    }
}