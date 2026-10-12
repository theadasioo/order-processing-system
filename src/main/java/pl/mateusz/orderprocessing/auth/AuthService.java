package pl.mateusz.orderprocessing.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mateusz.orderprocessing.auth.dto.AuthResponse;
import pl.mateusz.orderprocessing.auth.dto.LoginRequest;
import pl.mateusz.orderprocessing.auth.dto.RegisterRequest;
import pl.mateusz.orderprocessing.security.CustomUserDetailsService;
import pl.mateusz.orderprocessing.security.JwtService;
import pl.mateusz.orderprocessing.user.Role;
import pl.mateusz.orderprocessing.user.User;
import pl.mateusz.orderprocessing.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyTakenException(request.email());
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(request.email()));
        return new AuthResponse(token);
    }
}
