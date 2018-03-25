package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pt.isel.daw.g5.ChecklistAPI.exceptions.InvalidStateException;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.InvalidState;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<InvalidState> invalidState(IllegalArgumentException ex){
        InvalidParams invalidParams = new InvalidParams("state", "state must be either 'completed' or 'uncompleted", "finished");
        InvalidState invalidState = new InvalidState("/validation-error", "Your request parameters didn't validate.", 400, "The state value is not valid", "/checklists/1/checklistitems/3", new InvalidParams[]{invalidParams});
        //return invalidState;
        return new ResponseEntity<>(invalidState, HttpStatus.BAD_REQUEST);
    }
}
