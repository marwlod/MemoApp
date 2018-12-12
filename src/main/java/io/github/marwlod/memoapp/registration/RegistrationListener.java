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
        System.out.println("RegistrationListener -> onApplicationEvent()");
        this.confirmRegistration(event);
    }

    // extract all necessary User info from event, create verification token,
    // persist it and send as parameter in confirm registration link
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/confirmRegistration?token=" + token;
        System.out.println("before getting message");
        String message = messageSource.getMessage("message.registrationSuccess", null, "Registration was successful", event.getLocale());

        System.out.println("after getting message");
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + "http://localhost:8080" + confirmationUrl);
        System.out.println(email);
        System.out.println("Almost sending email");
        javaMailSender.send(email);
    }
}
