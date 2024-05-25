package com.Commerceapp.app.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
 
@Configuration
public class WebsecurityConfig   {
     
    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
 
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
         
        http.authenticationProvider(authenticationProvider());
         
        http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/dashboard").authenticated()
            .anyRequest().permitAll()
            )
            .formLogin(login -> {
                login
                        .loginPage("/login") // Specify your custom login page URL
                        .usernameParameter("email")
                        .defaultSuccessUrl("/2fa")
                        .permitAll();
            })
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll()
        );
         
        return http.build();
    }


}