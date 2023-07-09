package ru.endienasg.foxworld.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.endienasg.foxworld.security.filters.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("auth/**").permitAll().anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(anonimous->anonimous.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint((request, response, authException) ->{
                                    System.err.println("Failed! " + authException + " \n " + request +  " \n " + response);
                                    response.setStatus(401);
                                })
                                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                ).securityContext((securityContext) ->
                        securityContext.requireExplicitSave(false)
                ).build();
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .anonymous().disable()
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling(
//                        exception -> exception
//                                .authenticationEntryPoint((request, response, authException) ->{
//                                    System.err.println("Failed! " + authException + " \n " + request +  " \n " + response);
//                                })
//                                .accessDeniedHandler(new AccessDeniedHandlerImpl())
//                ).securityContext((securityContext) ->
//                        securityContext.requireExplicitSave(false)
//                );
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
