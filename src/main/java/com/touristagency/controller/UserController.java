package com.touristagency.controller;

import com.touristagency.dto.UpdateUserDTO;
import com.touristagency.entity.Tour;
import com.touristagency.entity.User;
import com.touristagency.entity.enums.Authority;
import com.touristagency.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller that react on user related requests
 *
 * @see User
 */
@Controller
@Log4j2
@RequestMapping
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public String getUsersPage(Model model,
                               @PageableDefault(sort = {"id"},
                                       direction = Sort.Direction.ASC,
                                       size = 5) Pageable pageable) {
        model.addAttribute("users", userService.findAllUsers(pageable));
        return "users";
    }

    @GetMapping("/users/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
        model.addAttribute("authorities", Authority.values());
        return "user-update";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("user") @Valid UpdateUserDTO userDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authorities", Authority.values());
            return "user-update";
        }
        userService.updateUser(id, userDTO);
        return "redirect:/users";
    }

    @GetMapping("/users/ban/{id}")
    public String banUser(@PathVariable("id") long id) {
        userService.banUser(id);
        return "redirect:/users";
    }

    @GetMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable("id") long id) {
        userService.unbanUser(id);
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String getUserProfilePage(@AuthenticationPrincipal User user,
                                     Model model) {
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "user-profile";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "error/404";
    }
}
