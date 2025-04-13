package com.userservice.config;

import com.userservice.constant.ResponseConstant;
import com.userservice.dto.CommonException;
import com.userservice.dto.UserPrincipal;
import com.userservice.repository.UserRepository;
import com.userservice.utility.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final List<String> AUTH_PATHS = List.of("/pub/", "/actuator/", "/h2-console");
    private static final List<String> OTP_PATH = List.of("/otp/");

    @Autowired
    private JwtTokenProvider jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            filterChain.doFilter(request, response);

    }

    private void handleException(HttpServletResponse response, Exception ex) throws IOException {
        // Set the response status and content type
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // Create an error response (JSON format)
        String jsonResponse = String.format("{\"message\": \"%s\", \"status\": \"%s\"}", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());

        // Write the error response
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
