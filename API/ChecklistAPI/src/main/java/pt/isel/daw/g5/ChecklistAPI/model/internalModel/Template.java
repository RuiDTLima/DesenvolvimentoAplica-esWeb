package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class Template {

    private Data[] data;

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Template(Data[] data) {
        this.data = data;
    }
}
