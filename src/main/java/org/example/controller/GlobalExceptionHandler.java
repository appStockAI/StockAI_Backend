package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@Slf4j
public class GlobalExceptionHandler {
    // 404 Error -> login page
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException e) {
        return "redirect:/api/users/login";
    }

    // root url("/") -> login page
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/api/users/login";
    }
}
