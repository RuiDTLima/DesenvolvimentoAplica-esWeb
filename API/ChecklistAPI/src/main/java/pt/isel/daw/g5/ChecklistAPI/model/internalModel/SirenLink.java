package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class SirenLink {
    private String[] rel;
    private String href;

    public String[] getRel() {
        return rel;
    }

    public void setRel(String[] rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public SirenLink(String[] rel, String href) {
        this.rel = rel;
        this.href = href;
    }
}