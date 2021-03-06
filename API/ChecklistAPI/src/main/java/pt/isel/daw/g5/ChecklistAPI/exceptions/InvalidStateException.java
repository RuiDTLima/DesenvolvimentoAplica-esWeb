package pt.isel.daw.g5.ChecklistAPI.exceptions;

import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;

public class InvalidStateException extends RuntimeException {
    private ProblemJSON problemJSON;

    public ProblemJSON getProblemJSON() {
        return problemJSON;
    }

    public void setProblemJSON(ProblemJSON problemJSON) {
        this.problemJSON = problemJSON;
    }

    public InvalidStateException(ProblemJSON problemJSON){
        this.problemJSON = problemJSON;
    }
}