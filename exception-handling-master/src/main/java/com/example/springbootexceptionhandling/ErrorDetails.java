package com.example.springbootexceptionhandling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
class ErrorDetails {

    private HttpStatus status;
    private Date timestamp;
    private String message;
    private String description;
    private List<ErrorMessage> errors;

    private ErrorDetails() {
        timestamp = new Date();
    }

    ErrorDetails(HttpStatus status) {
        this();
        this.status = status;
    }

    ErrorDetails(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.description = ex.getLocalizedMessage();
    }

    ErrorDetails(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.description = ex.getLocalizedMessage();
    }

    private void addSubError(ErrorMessage subError) {
        if (errors == null) {
        	errors = new ArrayList<>();
        }
        errors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ErrorMessage(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ErrorMessage(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }



    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    public class ErrorMessage{
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        ErrorMessage(String object, String message) {
            this.object = object;
            this.message = message;
        }
    }
}

class LowerCaseClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}