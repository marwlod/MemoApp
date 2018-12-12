package io.github.marwlod.memoapp.registration;

import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService userService;

    private MessageSource messageSource;

    private JavaMailSender javaMailSender;

    @Autowired
    public RegistrationListener(UserService userService, MessageSource messageSource, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        // listener triggering cascade of actions that lead up to sending email
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();

        // generate random token string
        String token = UUID.randomUUID().toString();
        //create token object and persist it
        userService.createVerificationToken(user, token);

        // prepare data to send
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/confirmRegistration?token=" + token;
        String message = messageSource.getMessage("message.reg.success-full", null, event.getLocale());

        // construct email from data and send it
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + "http://localhost:8080" + confirmationUrl);
        javaMailSender.send(email);
    }
}
