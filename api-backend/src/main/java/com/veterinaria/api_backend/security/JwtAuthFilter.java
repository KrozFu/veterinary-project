package com.veterinaria.api_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

//            System.out.println("UserDetails: " + userDetails);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtService.getSecretKey())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                List<String> roles = claims.get("roles", List.class);

                ////////// ---------------------
//                System.out.println("Roles del JWT: " + roles);
                ////////// ---------------------

                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> {
                            // Si el rol ya tiene ROLE_, no lo agregues de nuevo
                            if (role.startsWith("ROLE_")) {
                                return new SimpleGrantedAuthority(role);
                            }
                            return new SimpleGrantedAuthority("ROLE_" + role);
                        })
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//                System.out.println("Autenticando usuario: " + userDetails.getUsername());
//                System.out.println("Roles: " + authorities);
//                System.out.println(">>> Context antes de setAuthentication: " + SecurityContextHolder.getContext().getAuthentication());

                SecurityContextHolder.getContext().setAuthentication(authToken);
//                System.out.println(">>> Context después de setAuthentication: " + SecurityContextHolder.getContext().getAuthentication());
            }
        }

        filterChain.doFilter(request, response);
    }
}


//    @PostConstruct
//    public void init() {
//        System.out.println("JwtService: " + (jwtService != null ? "INYECTADO" : "NULL"));
//        System.out.println("UserDetailsService: " + (userDetailsService != null ? "INYECTADO" : "NULL"));
//    }
