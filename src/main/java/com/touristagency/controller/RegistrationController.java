package com.touristagency.controller;

import com.touristagency.dto.RegisterUserDTO;
import com.touristagency.service.UserService;
import com.touristagency.util.exception.UsernameNotUniqueException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegistrationPage(@ModelAttribute("user") RegisterUserDTO registerUserDTO) {
        return "registration";
    }

    @PostMapping
    public String registerNewUser(@ModelAttribute("user") @Valid RegisterUserDTO userDTO,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.createUser(userDTO);
        return "redirect:/login";
    }

    @ExceptionHandler(UsernameNotUniqueException.class)
    public String handleRuntimeException(UsernameNotUniqueException e,
                                         Model model) {
        model.addAttribute("user", new RegisterUserDTO());
        model.addAttribute("usernameErrorMessage", e.getMessage());
        return "registration";
    }
}
