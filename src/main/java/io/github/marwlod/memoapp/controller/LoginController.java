package io.github.marwlod.memoapp.controller;

import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
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
    public String createNewUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.email",
                            "There is already a user registered with the email provided");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveUser(user);
            model.addAttribute("successMessage", "User has been registered successfully");
            return "login";
        }
        return "register";
    }
}