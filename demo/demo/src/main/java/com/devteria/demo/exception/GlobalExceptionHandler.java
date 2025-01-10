package com.devteria.demo.exception;

import com.devteria.demo.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(Exception e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleUserExistedException(AppException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getErrorCode().getMessage());
        apiResponse.setCode(e.getErrorCode().getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        String enumKey = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try{
            errorCode=ErrorCode.valueOf(enumKey);
        }
        catch(Exception ex){

        }
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
