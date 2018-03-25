package pt.isel.daw.g5.ChecklistAPI.exceptions;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String exception){
        super(exception);
    }
}