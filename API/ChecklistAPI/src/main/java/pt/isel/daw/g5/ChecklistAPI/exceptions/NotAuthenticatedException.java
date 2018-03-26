package pt.isel.daw.g5.ChecklistAPI.exceptions;

import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;

public class NotAuthenticatedException extends RuntimeException {
    private ProblemJSON problemJSON;

    public ProblemJSON getProblemJSON() {
        return problemJSON;
    }

    public void setProblemJSON(ProblemJSON problemJSON) {
        this.problemJSON = problemJSON;
    }

    public NotAuthenticatedException(ProblemJSON problemJSON){
        this.problemJSON = problemJSON;
    }
}
