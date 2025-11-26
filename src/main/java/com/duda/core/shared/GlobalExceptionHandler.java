package com.duda.core.shared;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura qualquer erro inesperado
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericError(Exception ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Erro interno do servidor");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Trata erros de validação do DTO (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Trata erros de constraint gerados pelo banco:
     * - unique = true (placa duplicada)
     * - violação de not null
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String message = "Violação de integridade no banco de dados";

        if (ex.getCause() instanceof ConstraintViolationException constraint) {
            if (constraint.getConstraintName() != null &&
                    constraint.getConstraintName().toLowerCase().contains("placa")) {
                message = "A placa informada já existe";
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    /**
     * Quando um service dispara EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Exceptions comentadas no arquivo
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Recurso não encontrado");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Operação inválida");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
