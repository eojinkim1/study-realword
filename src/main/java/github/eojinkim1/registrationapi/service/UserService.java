package github.eojinkim1.registrationapi.service;

import github.eojinkim1.registrationapi.controller.dto.response.UserResponse;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.domain.UserRepository;
import github.eojinkim1.registrationapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void printAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("DB에 저장된 사용자가 없습니다.");
        } else {
            for (User user : users) {
                System.out.println("저장된 사용자 이메일: " + user.getEmail());
            }
        }
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
}
