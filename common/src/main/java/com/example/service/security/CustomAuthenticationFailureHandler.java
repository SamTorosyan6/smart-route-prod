package com.example.service.security;


import com.example.exception.AccountPendingException;
import com.example.exception.AccountRejectedException;
import com.example.exception.BlockedUserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String message;
        Throwable cause = exception.getCause();

        if (cause instanceof BlockedUserException ||
                cause instanceof AccountPendingException ||
                cause instanceof AccountRejectedException) {
            message = cause.getMessage();
        } else {
            message = "Invalid username or password";
        }

        response.sendRedirect("/loginPage?error=" + message);
    }
}
