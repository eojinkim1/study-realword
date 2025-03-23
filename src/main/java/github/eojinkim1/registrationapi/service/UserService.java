package github.eojinkim1.registrationapi.service;

import github.eojinkim1.registrationapi.controller.dto.request.UserUpdateRequest;
import github.eojinkim1.registrationapi.controller.dto.response.UserResponse;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.domain.UserRepository;
import github.eojinkim1.registrationapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User registration(String email, String password, String username) {
        return userRepository.save(
            User.registration(
                    email,
                    password,
                    username
            )
        );
    }

    public UserResponse login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("이메일이 존재하지 않습니다."));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new UserResponse(
                user.getEmail(),
                token,
                user.getUsername(),
                user.getBio(),
                user.getImage()
        );
    }

    public UserResponse getCurrentUser(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String userBio = user.getBio() != null ? user.getBio() : "I work at statefarm";

        return new UserResponse(
                user.getEmail(),
                token,
                user.getUsername(),
                userBio,
                user.getImage()
        );
    }

    public UserResponse updateUser(String email, UserUpdateRequest updateRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user = user.updateUser(
                updateRequest.username(),
                updateRequest.password(),
                updateRequest.bio(),
                updateRequest.image()
        );

        userRepository.save(user);

        return new UserResponse(
                user.getEmail(),
                null,
                user.getUsername(),
                user.getBio(),
                user.getImage()
        );

    }
}
