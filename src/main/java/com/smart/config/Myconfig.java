package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Myconfig  {

    @Bean
    public UserDetailsService getUserDetailService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider(getUserDetailService());
    	provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/**").permitAll()
            )
            .formLogin(form -> form
                .loginPage("/signin")              // Form dikhane wala URL
                .loginProcessingUrl("/dologin")      // Aapke HTML action ke matching
                .defaultSuccessUrl("/user/index")   // Login ke baad sahi rasta
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/signin?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        http.authenticationProvider(authenticationProvider());

        return http.build();
    } 


    
}