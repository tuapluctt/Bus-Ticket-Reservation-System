package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.hvt.spring.BusTicketReservationSystem.service.EmailSevice;
import vn.hvt.spring.BusTicketReservationSystem.util.EmailUtils;

import java.util.Map;
import java.util.Objects;

@Service
public class EmailSeviceIpml implements EmailSevice {
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String SUCCESSFUL_TICKET = "Email confirmation of successful ticket purchase";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "public/emailtemplate";
    public static final String EMAIL_COMFIRMATION_OF_SUCCESSFUL_TICKET = "public/email-successful-ticket";
    public static final String TEXT_HTML_ENCONDING = "text/html";

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
    public void sendHtmlEmail(String phonenumber, String to, String token) {
        try {
            Context context = new Context();
            context.setVariables(Map.of("name", to, "url", EmailUtils.getVerificationUrl(to,token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void sendEmailConfirmationOfSuccessfulTicket(String to, Map<String, Object> info) {
        try {
            Context context = new Context();
            // truyen map du lieu vaof tempalate
            context.setVariables(info);

            String text = templateEngine.process(EMAIL_COMFIRMATION_OF_SUCCESSFUL_TICKET, context);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(SUCCESSFUL_TICKET);
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
}
