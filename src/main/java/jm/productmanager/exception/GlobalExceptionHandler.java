package jm.productmanager.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(BlockedWordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBlockedWordNotFoundException(BlockedWordNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getCode()));
    }

    private ErrorResponse createErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(status.getReasonPhrase());
        errorResponse.setCode(status.value());
        return errorResponse;
    }
}
