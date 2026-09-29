//package com.yantra.jwt;
//
//import com.yantra.entity.User;
//import com.yantra.repositroy.UserRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserRepository userRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");
//
//        if (header != null && header.startsWith("Bearer ")) {
//            String token = header.substring(7);
//
//            if (jwtUtil.isValid(token)) {
//                String email = jwtUtil.extractEmail(token);
//                Optional<User> userOpt = userRepository.findByEmail(email);
//
//                if (userOpt.isPresent()) {
//                    User user = userOpt.get();
//                    var authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
//                    var authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}




package com.yantra.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET;

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            String role  = claims.get("role", String.class);

//            logger.info("EMAIL = " + email);
            System.out.println("EMAIL = " + email);
            System.out.println("ROLE  = " + role);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                List<SimpleGrantedAuthority> authorities = List.of(
//                        new SimpleGrantedAuthority("ROLE_" + role)
//                );
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority(role)
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            System.out.println("TOKEN PARSE ERROR = " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }



}