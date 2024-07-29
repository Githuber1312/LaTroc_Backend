package dev.artep.latroc_backend_simple.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import dev.artep.latroc_backend_simple.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final LocalUserDAO localUserDAO;

    public JWTRequestFilter(JWTService jwtService, LocalUserDAO localUserDAO) {
        this.jwtService = jwtService;
        this.localUserDAO = localUserDAO;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String username = jwtService.getUsername(token);
                if (username != null) {
                    Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
                    if (opUser.isPresent()) {
                        LocalUser user = opUser.get();
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        /*System.out.println("User not found: " + username); // Debugging*/
                    }
                } else {
                    /*System.out.println("Username extracted from token is null"); // Debugging*/
                }
            } catch (JWTDecodeException ex) {
                /*System.out.println("JWT Decode Exception: " + ex.getMessage()); // Debugging*/
            }
        } else {
            /*System.out.println("Authorization header missing or does not start with Bearer "); // Debugging*/
        }
        filterChain.doFilter(request, response);
    }
}
