package vn.hvt.spring.BusTicketReservationSystem.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import vn.hvt.spring.BusTicketReservationSystem.exception.AccountNotVerifiedException;

import java.io.IOException;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String url ;
        if (exception instanceof InternalAuthenticationServiceException) {
            System.out.println("Authentication failed: " + exception.getMessage());
            url = "/login?error="+exception.getMessage();
        } else {
            url = "/login?error="+exception.getMessage();
        }
        response.sendRedirect( request.getContextPath() + url);
    }
}
