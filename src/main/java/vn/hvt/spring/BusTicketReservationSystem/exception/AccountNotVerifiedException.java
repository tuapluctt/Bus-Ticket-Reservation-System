package vn.hvt.spring.BusTicketReservationSystem.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}