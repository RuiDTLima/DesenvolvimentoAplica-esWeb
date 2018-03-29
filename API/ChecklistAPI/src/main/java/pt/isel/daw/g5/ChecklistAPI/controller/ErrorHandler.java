package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pt.isel.daw.g5.ChecklistAPI.exceptions.InvalidStateException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.NotAuthenticatedException;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ProblemJSON> invalidState(InvalidStateException ex){
        return new ResponseEntity<>(ex.getProblemJSON(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ProblemJSON> notAuthenticated(NotAuthenticatedException ex){
        return new ResponseEntity<>(ex.getProblemJSON(), HttpStatus.UNAUTHORIZED);
    }
}