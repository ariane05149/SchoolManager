package com.example.schoolmanager.security;

import com.example.schoolmanager.auth.JwtUtil;
import com.example.schoolmanager.user.AuthProvider;
import com.example.schoolmanager.user.Role;
import com.example.schoolmanager.user.User;
import com.example.schoolmanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) auth.getPrincipal();
        Map<String,Object> attributes = oauthUser.getAttributes();

        String email = (String) attributes.get("email");
        String login = (String) attributes.get("login"); // GitHub username
        String name = (String) attributes.getOrDefault("name", login != null ? login : email);

        if (email == null && login != null) {
            email = login + "@github.local";
        }

        AuthProvider provider = attributes.containsKey("sub") ? AuthProvider.GOOGLE : AuthProvider.GITHUB;

        // Final vars for lambda
        final String finalEmail = email;
        final String finalName = name;
        final AuthProvider finalProvider = provider;

        // Auto-create user if not exists
        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(finalEmail)
                        .name(finalName)
                        .role(Role.STUDENT) // default role for OAuth2
                        .provider(finalProvider)
                        .password("") // OAuth2 users don't need password
                        .build()
                ));

        // Generate JWT
        String token = jwtUtil.generateToken(finalEmail);

        // Redirect to Swagger with token
        String redirectUrl = "http://localhost:8080/swagger-ui/index.html?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}