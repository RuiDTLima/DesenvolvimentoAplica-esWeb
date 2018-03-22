package pt.isel.daw.g5.ChecklistAPI.model;

public class Data {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Data(String name, String value) {
        this.name = name;
        this.value = value;
    }
}