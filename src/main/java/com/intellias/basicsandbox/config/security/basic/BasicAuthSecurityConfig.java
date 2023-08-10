package com.intellias.basicsandbox.config.security.basic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@PropertySource("classpath:credentials.properties")
public class BasicAuthSecurityConfig {
    @Value("${app.security.enabled}")
    private boolean securityEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (securityEnabled) {
            http.authorizeHttpRequests().anyRequest().authenticated()
                    .and().httpBasic()
                    .and().sessionManagement().disable();
        } else {
            // The CSRF protection is disabled due to the following recommendation:
            // https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html#when-to-use-csrf-protection

            http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
        }

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(@Value("${sandox.user.login}") String login,
                                                         @Value("${sandox.user.password}") String password,
                                                         @Value("${sandox.user.role}") String role) {
        UserDetails user = User.builder()
                .username(login)
                .password(encoder().encode(password))
                .roles(role)
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
