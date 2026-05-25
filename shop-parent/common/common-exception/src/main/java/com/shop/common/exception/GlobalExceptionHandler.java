package com.shop.common.exception;

import com.shop.common.entity.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException e, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(e.getCode(), e.getMessage());
        String uri = request.getDescription(false).replace("uri=", "");
        if (!uri.isEmpty()) {
            problem.setInstance(java.net.URI.create(uri));
        }
        return ResponseEntity.status(e.getCode()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidException(MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ProblemDetail.forStatusAndDetail(400, detail);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleException(Exception e) {
        return ProblemDetail.forStatusAndDetail(500, "服务器内部错误: " + e.getMessage());
    }
}
