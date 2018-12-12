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
    public LoginController(UserService userService, ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource) {
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
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.email",
                            "There is already a user registered with the email provided");
        }
        if (!bindingResult.hasErrors()) {
            User registered = userService.saveUser(user);
            try {
                String appUrl = webRequest.getContextPath();
                applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, webRequest.getLocale(), appUrl));
            } catch (Exception e) {
                model.addAttribute("emailError", "Java mail configuration error");
                return "register";
            }
            model.addAttribute("successMessage", "User has been registered successfully");
            return "login";
        }
        return "register";
    }

    @GetMapping(value = "/confirmRegistration")
    public String confirmRegistration(@RequestParam("token") String token, Model model, WebRequest webRequest) {
        Locale locale = webRequest.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("verification.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "user-not-verified";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime()) <= 0) {
            String message = messageSource.getMessage("verification.message.expiredToken", null, locale);
            model.addAttribute("message", message);
            return "user-not-verified";
        }
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return "login";
    }
}