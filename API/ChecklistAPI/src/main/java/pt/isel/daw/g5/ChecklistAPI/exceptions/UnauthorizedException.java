package pt.isel.daw.g5.ChecklistAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private ProblemJSON problemJSON;

    public ProblemJSON getProblemJSON() {
        return problemJSON;
    }

    public void setProblemJSON(ProblemJSON problemJSON) {
        this.problemJSON = problemJSON;
    }

    public UnauthorizedException(ProblemJSON problemJSON){
        this.problemJSON = problemJSON;
    }
}