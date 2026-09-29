package com.yantra.config;

import com.yantra.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {
                })

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

//package com.yantra.config;
//
//import com.yantra.jwt.JwtAuthFilter;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfig()))
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//
//                        // Category & Product: sirf ADMIN/MANAGER create/update/delete kar sakte hain
//                        .requestMatchers("POST", "/api/categories/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER")
//                        .requestMatchers("PUT", "/api/categories/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER")
//                        .requestMatchers("DELETE", "/api/categories/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER")
//
//                        .requestMatchers("POST", "/api/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER", "ROLE_BRANCH_MANAGER")
//                        .requestMatchers("PUT", "/api/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER", "ROLE_BRANCH_MANAGER")
//                        .requestMatchers("DELETE", "/api/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER")
//
//                        // Orders: Cashier bhi bana sakta hai (billing), sab authenticated dekh sakte hain
//                        .requestMatchers("POST", "/api/orders/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STORE_MANAGER", "ROLE_BRANCH_MANAGER", "ROLE_CASHIER")
//
//                        // GET requests: koi bhi logged-in user (Cashier bhi products/orders dekh sake)
//                        .requestMatchers("GET", "/api/**").authenticated()
//
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    private CorsConfigurationSource corsConfig() {
//        CorsConfiguration cfg = new CorsConfiguration();
//        cfg.setAllowedOrigins(List.of("http://localhost:5173"));
//        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        cfg.setAllowedHeaders(List.of("*"));
//        cfg.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", cfg);
//        return source;
//    }
//}
