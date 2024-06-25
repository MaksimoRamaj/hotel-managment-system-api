package com.example.hotelManagmentSystem.config;

import com.example.hotelManagmentSystem.dataproviders.repository.TokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   TokenRepository tokenRepository) throws Exception {

        http.cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()
                        ))
                .csrf((httpSecurityCsrfConfigurer)->httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/auth/**")
                        .permitAll()
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/swagger-ui.html","swagger-ui/swagger-ui/index.html")
                        .permitAll()
                        .requestMatchers("api/hotel-services/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sesion->sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logout -> logout
                                .logoutUrl("/api/auth/logout")
                                .permitAll()
                                .addLogoutHandler((request, response, authentication) -> {
                                    final String authHeader = request.getHeader("Authorization");
                                    if (authHeader == null || !authHeader.startsWith("Bearer ")){
                                        return;
                                    }
                                    final String jwtToken = authHeader.substring(7);;
                                    var storedToken =
                                            tokenRepository.findByToken(jwtToken)
                                                    .orElse(null);
                                    if(storedToken != null){
                                        storedToken.setExpired(true);
                                        storedToken.setRevoked(true);
                                        tokenRepository.save(storedToken);
                                    }
                                })
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    SecurityContextHolder.clearContext();
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"message\": \"Logout successful\"}");
                                })
                )
        ;

        return http.build();
    }
}
