package vn.hvt.spring.BusTicketReservationSystem.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public String handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex, RedirectAttributes redirectAttributes) {
        String errorMessage = "Lỗi xác thực: " + ex.getCause().getMessage();
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return "redirect:/login";
    }
}