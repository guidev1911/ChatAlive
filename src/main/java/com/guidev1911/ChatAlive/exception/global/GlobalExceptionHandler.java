package com.guidev1911.ChatAlive.exception.global;

import com.guidev1911.ChatAlive.dto.responses.ApiResponse;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.ConfirmationCodeAlreadySentException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.ConfirmationCodeExpiredException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.EmailAlreadyRegisteredException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.InvalidConfirmationCodeException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupAccessException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupAlreadyExistsException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupNotFoundException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupRequestException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.userExceptions.ImageStorageException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.userExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(false, ex.getMessage()));
    }
    @ExceptionHandler(GroupAccessException.class)
    public ResponseEntity<ApiResponse> handleGroupAccess(GroupAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, ex.getMessage()));
    }
    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<ApiResponse> handleImageStorageException(ImageStorageException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(GroupRequestException.class)
    public ResponseEntity<ApiResponse> handleGroupRequest(GroupRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }
    @ExceptionHandler(GroupAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleGroupAlreadyExists(GroupAlreadyExistsException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(false, ex.getMessage()));
    }
    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ApiResponse> handleGroupNotFound(GroupNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, ex.getMessage()));
    }
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ApiResponse> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(ConfirmationCodeAlreadySentException.class)
    public ResponseEntity<ApiResponse> handleConfirmationCodeAlreadySent(ConfirmationCodeAlreadySentException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InvalidConfirmationCodeException.class)
    public ResponseEntity<ApiResponse> handleInvalidCode(InvalidConfirmationCodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(ConfirmationCodeExpiredException.class)
    public ResponseEntity<ApiResponse> handleExpiredCode(ConfirmationCodeExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Erro interno: " + ex.getMessage()));
    }
}
