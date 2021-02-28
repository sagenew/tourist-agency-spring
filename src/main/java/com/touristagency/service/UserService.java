package com.touristagency.service;

import com.touristagency.dto.RegisterUserDTO;
import com.touristagency.dto.UpdateUserDTO;
import com.touristagency.entity.Order;
import com.touristagency.entity.User;
import com.touristagency.entity.enums.Authority;
import com.touristagency.repository.UserRepository;
import com.touristagency.util.exception.UsernameNotUniqueException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Service
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo,
                       MessageSource messageSource,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public void createUser(RegisterUserDTO userDTO) {
        User user = User
                .builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .bio(userDTO.getBio())
                .authorities(Collections.singleton(Authority.USER))
                .currentDiscount(0.0)
                .registrationDate(LocalDateTime.now())
                .enabled(true)
                .build();
        try {
            userRepo.save(user);
            log.info("New user " + user);
        } catch (DataIntegrityViolationException e) {
            log.error("Login not unique: " + userDTO.getUsername());
            throw new UsernameNotUniqueException(messageSource.getMessage(
                    "users.registration.login.not_unique",
                    null,
                    LocaleContextHolder.getLocale()) + userDTO.getUsername(), e);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateUser(long id, UpdateUserDTO userDTO) {
        User user = getUserById(id);
        user.setUsername(userDTO.getUsername());
        if (Objects.nonNull(userDTO.getPassword()) && userDTO.getPassword().length() > 0) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setCurrentDiscount(userDTO.getCurrentDiscount());
        user.setBio(user.getBio());
        user.setAuthorities(userDTO.getAuthorities());

        try {
            userRepo.save(user);
            log.info("Updated user " + user);
        } catch (DataIntegrityViolationException e) {
            log.error("Login not unique: " + userDTO.getUsername());
            throw new UsernameNotUniqueException(messageSource.getMessage(
                    "users.registration.login.not_unique",
                    null,
                    LocaleContextHolder.getLocale()) + userDTO.getUsername(), e);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void banUser(long id) {
        User user = getUserById(id);
        user.setEnabled(false);
        userRepo.save(user);
        log.info("Banned user " + user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void unbanUser(long id) {
        User user = getUserById(id);
        user.setEnabled(true);
        userRepo.save(user);
        log.info("Unbanned user " + user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteUser(long id) {
        User user = getUserById(id);
        for (Order order : user.getOrders()) {
            order.getTour().getOrders().remove(order);
        }
        log.info("Deleted user " + user);
        userRepo.delete(user);
    }


    public User getUserById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));
    }
}
