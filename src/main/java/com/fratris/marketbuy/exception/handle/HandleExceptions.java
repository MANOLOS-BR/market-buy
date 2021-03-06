package com.fratris.marketbuy.exception.handle;

import com.fratris.marketbuy.exception.ApiError;
import com.fratris.marketbuy.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandleExceptions {

  @ExceptionHandler({ ConstraintViolationException.class })
  public ResponseEntity<Object> handleConstraintViolation(
          ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " " +
              violation.getPropertyPath() + ": " + violation.getMessage());
    }

    ApiError apiError =
            new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    return new ResponseEntity<>(
            apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({NotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleDataException(NotFoundException ex){
    return  ResponseEntity.badRequest().body(ex.getMessage());
  }

}
