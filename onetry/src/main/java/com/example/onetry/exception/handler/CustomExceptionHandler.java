package com.example.onetry.exception.handler;

import com.example.onetry.common.CommonResponseDto;
import com.example.onetry.exception.CustomException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

    //@Valid 유효성검사 실패 시
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        log.error(errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDto("올바르지 않은 입력값입니다",errors));
    }

    // 역직렬화 과정에서 dto필드의 타입이 맞지 않아 발생하는 예외, PathVariable이나 RequestParam으로 받을 때 타입이 맞지않은 경우와 다른 예외를 던지기 떄문에 분리
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("java.lang.Long"))
            errorMessage = ": Long타입 예외";
        else if(errorMessage.contains("Enum"))
            errorMessage = ": ENUM타입 예외";
        else
            errorMessage = ": 기타 타입예외";

        String message = "올바른 요청타입이 아닙니다.";
        log.error(message+errorMessage+" - "+ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDto(message+errorMessage,null));
    }


    // custom예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponseDto> handleCustomException(CustomException ex){
        log.error("예외가 발생했습니다.:"+ex.getExceptionCode().getMessage());
        return ResponseEntity.status(ex.getExceptionCode().getStatus()).body(new CommonResponseDto(ex.getExceptionCode().getMessage(),null));
    }

    // requestParam으로 입력받은 값의 유효성검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponseDto> handleContranintViolation(ConstraintViolationException ex){
        String msg = ex.getMessage();
        int index = msg.indexOf(":");

        if (index >= 0) {
            msg = msg.substring(index + 1).trim();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDto(msg,null));
    }

    // @PathVariable로 입력받은 값의 타입이 올바르지 않을 때
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponseDto> handleMethodArgTypeException(MethodArgumentTypeMismatchException ex){

        String fieldName = ex.getName();
        String requiredType = ex.getRequiredType().getSimpleName();
        String message = fieldName+"이 "+requiredType+"타입이여야 합니다.";
        log.error("URI값이 올바르지 않습니다. - "+ message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDto(message,null));
    }

    // 기타예외 발생 시 500반환
    @ExceptionHandler
    public ResponseEntity<CommonResponseDto> handleException(Exception ex) {

        String message = "서버 내부에 에러가 발생했습니다.";
        log.error(message+":"+ex.getMessage()+ex.getStackTrace()+ex.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDto(message,null));
    }

}
