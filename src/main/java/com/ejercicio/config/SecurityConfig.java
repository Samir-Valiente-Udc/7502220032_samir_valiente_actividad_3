package com.ejercicio.config;

import com.ejercicio.service.impl.UsuarioServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UsuarioServiceImpl usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/index", "/login", "/css/**", "/js/**", "/registro","/recovery-password", "/recoverypass", "/usuarios/recuperarPassword").permitAll() 
                        .anyRequest().authenticated() 
                )
                .formLogin(form -> form
                        .loginPage("/login") 
                        .defaultSuccessUrl("/index", true) 
                        .failureUrl("/login?error=true") 
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") 
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}