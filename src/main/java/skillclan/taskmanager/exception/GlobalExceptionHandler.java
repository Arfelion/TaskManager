package skillclan.taskmanager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalHandleException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        ErrorResponse er = new ErrorResponse("Internal server error", ex.getMessage());
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("An EntityNotFoundException occurred: {}", ex.getMessage());
        ErrorResponse er = new ErrorResponse("Entity not found exception", ex.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = {IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
        logger.error("An BadRequestException occurred: {}", ex.getMessage());
        ErrorResponse er = new ErrorResponse("Bad request exception", ex.getMessage());
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex){
        logger.error("A DuplicateKeyException occurred: {}", ex.getMessage());
        int indexOfDetail = ex.getMessage().indexOf("Detail:");
        ErrorResponse er = new ErrorResponse("A resource with this unique identifier already exists", ex.getMessage().substring(indexOfDetail).trim());
        return new ResponseEntity<>(er, HttpStatus.CONFLICT);
    }

}
