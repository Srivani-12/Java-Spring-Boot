package com.example.course.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurity {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;



//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.setUsersByUsernameQuery(
//                "select user_id, pw, active from members where user_id=?"
//        );
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
//                "select user_id, role from roles where user_id=?"
//        );
//        return jdbcUserDetailsManager;
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http.authorizeHttpRequests(configurer->
                configurer
                        .requestMatchers("/register", "/registerForm", "/css/**").permitAll()
                        .requestMatchers("/").hasRole("ADMIN")
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
                        .requestMatchers("/systems/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form->form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .permitAll())
                .logout(logout->logout.permitAll())
                .exceptionHandling(configurer->configurer.accessDeniedPage("/access-denied"));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}