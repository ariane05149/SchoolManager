package com.example.schoolmanager.auth;

import com.example.schoolmanager.user.AuthProvider;
import com.example.schoolmanager.user.Role;
import com.example.schoolmanager.user.User;
import com.example.schoolmanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId().toLowerCase();

        String email = oAuth2User.getAttribute("email");      // Google gives this
        String login = oAuth2User.getAttribute("login");      // GitHub username
        String name  = oAuth2User.getAttribute("name");       // Display name

        AuthProvider provider =
                registrationId.equals("google") ? AuthProvider.GOOGLE :
                        registrationId.equals("github") ? AuthProvider.GITHUB :
                                AuthProvider.LOCAL;

        // GitHub fallback
        if (email == null && login != null) {
            email = login + "@github.local";
        }

        // Name fallback
        if (name == null) {
            name = login != null ? login : "UnknownUser";
        }

        Optional<User> existing = userRepository.findByEmail(email);

        if (existing.isEmpty()) {
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .password("")       // No password for OAuth users
                    .role(Role.STUDENT)
                    .provider(provider)
                    .build();

            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}