package com.geekhub.secretaryhelperapp.user.controller;

import com.geekhub.secretaryhelperapp.calendar.model.Calendar;
import com.geekhub.secretaryhelperapp.calendar.service.CalendarService;
import com.geekhub.secretaryhelperapp.user.model.User;
import com.geekhub.secretaryhelperapp.user.model.UserDto;
import com.geekhub.secretaryhelperapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class AuthController {

        private final UserService userService;

        private final CalendarService calendarService;

        @Autowired
        public AuthController(final UserService userService, final CalendarService calendarService) {
            this.userService = userService;
            this.calendarService = calendarService;
        }

        @GetMapping("/access-denied")
        public String getAccessDeniedPage() {
            return "user/access-denied";
        }

        @GetMapping(value = {"/", "/login"})
        public String login() {
            return "user/login";
        }

        @GetMapping("/registration")
        public String getRegistrationPage(Model model) {
            model.addAttribute("user", new UserDto());
            model.addAttribute("departments", calendarService.getAll()
                    .stream()
                    .map(Calendar::getName)
                    .collect(Collectors.toUnmodifiableSet())
            );
            return "user/registration";
        }

        @PostMapping("/registration")
        public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
            if (userService.getUserByUsername(user.getUsername()).isPresent()) {
                bindingResult.rejectValue("username", "error.user", "Username is already used");
            }
            if (userService.getUserByEmail(user.getEmail()).isPresent()) {
                bindingResult.rejectValue("email", "error.email", "Email is already used");
            }
            if (bindingResult.hasFieldErrors("displayName")) {
                bindingResult.rejectValue("displayName", "error.displayName",
                        "Display name should contain only Latin letters and spaces");
            }
            if (bindingResult.hasErrors()) {
                model.addAttribute("departments", calendarService.getAll()
                        .stream()
                        .map(Calendar::getName)
                        .collect(Collectors.toUnmodifiableSet()));
            } else {
                userService.saveUser(user);
                model.addAttribute("successMessage", "You successfully have been registered");
            }
            return "user/registration";
        }
}
