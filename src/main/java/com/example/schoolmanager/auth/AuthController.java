package com.example.schoolmanager.auth;

import com.example.schoolmanager.user.User;
import com.example.schoolmanager.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    private static final String REGISTER_LINK =
            "Use this endpoint to register: /students/register (Swagger → Students → Register)";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // Check if account exists
        User user = userService.getByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(404).body(
                    new AuthResponse("Account does not exist. Please register first. " + REGISTER_LINK)
            );
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Auth successful
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                    new AuthResponse("Invalid credentials. If you don't have an account, register here: " + REGISTER_LINK)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new AuthResponse("Something went wrong. Please try again.")
            );
        }
    }
}