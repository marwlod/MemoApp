package io.github.marwlod.memoapp.controller;

import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.entity.VerificationToken;
import io.github.marwlod.memoapp.registration.OnRegistrationCompleteEvent;
import io.github.marwlod.memoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;

@Controller
public class LoginController {

    private UserService userService;

    private ApplicationEventPublisher applicationEventPublisher;

    private MessageSource messageSource;

    @Autowired
    public LoginController(UserService userService,
                           ApplicationEventPublisher applicationEventPublisher,
                           MessageSource messageSource) {
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
    }

    @GetMapping(value="/login")
    public String login(){
        return "login";
    }

    @GetMapping(value= "/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String createNewUser(@Valid @ModelAttribute("user") User user,
                                BindingResult bindingResult,
                                Model model,
                                WebRequest webRequest) {

        // check if email already in database
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "message.reg.email-exist");
        }

        // error-free path
        if (!bindingResult.hasErrors()) {
            // save (not enabled yet) user to database
            User registered = userService.saveUser(user);
            Locale locale = webRequest.getLocale();
            try {
                String appUrl = webRequest.getContextPath();
                // tell the listener that it can start preparing email to send
                applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, webRequest.getLocale(), appUrl));
            } catch (Exception e) {
                // the listener couldn't hear our prayers
                String message = messageSource.getMessage("message.reg.email-error", null, locale);
                model.addAttribute("emailNotSent", message);
                return "register";
            }
            // email sent
            String message = messageSource.getMessage("message.reg.success", null, locale);
            model.addAttribute("successMessage", message);
            return "login";
        }

        // if any errors detected try again (displaying error messages handled by thymeleaf)
        return "register";
    }

    @GetMapping(value = "/confirmRegistration")
    public String confirmRegistration(@RequestParam("token") String token,
                                      Model model,
                                      WebRequest webRequest) {
        Locale locale = webRequest.getLocale();

        // try to get token object, if failed -> invalid token string
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("message.reg.invalid-token", null, locale);
            model.addAttribute("failMessage", message);
            return "user-not-verified";
        }

        Calendar calendar = Calendar.getInstance();
        // if token expired
        if ((verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime()) <= 0) {
            String message = messageSource.getMessage("message.reg.expired-token", null, locale);
            model.addAttribute("failMessage", message);
            return "user-not-verified";
        }

        // user verified -> update in database
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        String message = messageSource.getMessage("message.reg.verified", null, locale);
        model.addAttribute("successMessage", message);
        return "login";
    }
}