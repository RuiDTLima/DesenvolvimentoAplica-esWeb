package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class ChecklistTemplatesHome {
    private String href;
    private Hints hints;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Hints getHints() {
        return hints;
    }

    public void setHints(Hints hints) {
        this.hints = hints;
    }

    public ChecklistTemplatesHome(String href, Hints hints) {
        this.href = href;
        this.hints = hints;
    }
}