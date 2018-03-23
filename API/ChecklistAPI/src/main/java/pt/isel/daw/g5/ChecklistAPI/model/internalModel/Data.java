package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

public class Data {
    private String name;
    private String value;
    private String prompt;

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

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Data(String name, String value, String prompt) {
        this.name = name;
        this.value = value;
        this.prompt = prompt;
    }
}