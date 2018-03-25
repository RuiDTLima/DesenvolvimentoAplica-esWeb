package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class Item {
    private String href;
    private Data[] data;
    private CollectionLink[] collectionLinks;

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

    public CollectionLink[] getCollectionLinks() {
        return collectionLinks;
    }

    public void setCollectionLinks(CollectionLink[] collectionLinks) {
        this.collectionLinks = collectionLinks;
    }

    public Item(String href, Data[] data, CollectionLink[] collectionLinks) {
        this.href = href;
        this.data = data;
        this.collectionLinks = collectionLinks;
    }
}