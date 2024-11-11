package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.hvt.spring.BusTicketReservationSystem.enums.EmailType;
import vn.hvt.spring.BusTicketReservationSystem.service.EmailSevice;

import java.util.Map;

@Service
public class EmailSeviceIpml implements EmailSevice {
    public static final String UTF_8_ENCODING = "UTF-8";

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;



    public EmailSeviceIpml() {
    }

    @Override
    public void sendSimpleMailMessage(String name, String to, String token) {
    }

    @Override
    public void sendMimeMessageWithAttachments(String name, String to, String token) {

    }

    @Override
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {

    }
    @Override
    public void sendHtmlEmail(String to, EmailType emailType, Map<String, Object> info) {
        try {
            Context context = new Context();

            context.setVariables(info);

            String text = templateEngine.process(getTemplateName(emailType), context);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(getSubject(emailType));
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }


    @Override
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {

    }

    private String getSubject(EmailType emailType) {
        switch (emailType) {
            case REMINDER_TRIP:
                return "[XURYBUS] Nhắc nhở chuyến xe";
            case TICKET_CONFIRMATION:
                return "[XURYBUS] Mua vé xe thành công";
            case ACCOUNT_REGISTRATION_CONFIRMATION:
                return "[XURYBUS] Xác nhận đăng ký tài khoản XURYBUS";
            default:
                return "Thông báo";
        }
    }

    private String getTemplateName(EmailType emailType) {
        switch (emailType) {
            case REMINDER_TRIP:
                return "public/reminder_email";
            case TICKET_CONFIRMATION:
                return "public/email-successful-ticket";
            case ACCOUNT_REGISTRATION_CONFIRMATION:
                return "public/emailtemplate";
            default:
                return "default_template";
        }
    }
}
