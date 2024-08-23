package com.wefood.front.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.services.ssm.endpoints.internal.Value;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 모든 예외를 처리하는 메서드
    @ExceptionHandler(Exception.class)
    public final String handleAllExceptions(Exception ex, Model model) {
        // 예외 메시지 생성
        String errorMessage = ex.getMessage();
        // 커스텀 에러 응답 객체 생성

        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }


}

