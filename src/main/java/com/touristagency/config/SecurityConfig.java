package com.touristagency.config;

import com.touristagency.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PasswordEncoder passwordEncoder;

    SecurityConfig(UserService userService, AccessDeniedHandler accessDeniedHandler, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/","/index", "/tours", "/js/**", "/images/**", "/css/**", "/access-denied", "/favicon.ico", "/error/**")
                .permitAll()
                .antMatchers("/users/**", "/tours/add", "/tours/update/**","/tours/delete/**")
                .hasAuthority("ADMIN")
                .antMatchers("/tours/make-hot/**", "/tours/orders", "/tours/orders/set-discount",  "/tours/orders/mark-paid/**", "/tours/orders/mark-denied/**")
                .hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/profile",  "/tours/orders/add/**", "/tours/orders/delete/**")
                .hasAnyAuthority("ADMIN", "MANAGER", "USER")
                .antMatchers("/registration", "/login")
                .anonymous()
                .anyRequest()
                .denyAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
