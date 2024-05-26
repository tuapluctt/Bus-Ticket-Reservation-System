package vn.hvt.spring.BusTicketReservationSystem.service;

import java.util.Map;
import java.util.Objects;

public interface EmailSevice {
    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachments(String name, String to, String token);
    void sendMimeMessageWithEmbeddedFiles(String name, String to, String token);
    void sendHtmlEmail(String name, String to, String token);
    void sendEmailConfirmationOfSuccessfulTicket(String to, Map<String, Object> info);
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}
