package com.example.schoolmanager.auth;

import com.example.schoolmanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class AuthFormController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/auth/login-form")
    public RedirectView loginForm(@RequestParam String email,
                                  @RequestParam String password) {

        try {
            // Check if user exists
            if (!userRepository.existsByEmail(email)) {
                return new RedirectView("/login.html?error=no_account_register");
            }

            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Generate JWT
            String token = jwtUtil.generateToken(userDetails.getUsername());

            // Redirect to Swagger with token
            return new RedirectView("http://localhost:8080/swagger-ui/index.html?token=" + token);

        } catch (BadCredentialsException ex) {
            return new RedirectView("/login.html?error=invalid_credentials_register");
        } catch (Exception ex) {
            return new RedirectView("/login.html?error=unknown");
        }
    }
}