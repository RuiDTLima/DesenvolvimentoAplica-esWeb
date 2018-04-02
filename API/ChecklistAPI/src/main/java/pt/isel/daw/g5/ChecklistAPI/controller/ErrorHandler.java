package pt.isel.daw.g5.ChecklistAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pt.isel.daw.g5.ChecklistAPI.exceptions.InvalidStateException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.ForbiddenException;
import pt.isel.daw.g5.ChecklistAPI.exceptions.UnauthorizedException;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ProblemJSON> invalidState(InvalidStateException ex){
        return new ResponseEntity<>(ex.getProblemJSON(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ProblemJSON> notAuthenticated(ForbiddenException ex){
        return new ResponseEntity<>(ex.getProblemJSON(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemJSON> notAuthenticated(UnauthorizedException ex){
        return new ResponseEntity<>(ex.getProblemJSON(), HttpStatus.UNAUTHORIZED);
    }
}