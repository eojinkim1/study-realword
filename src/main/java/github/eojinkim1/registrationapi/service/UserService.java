package github.eojinkim1.registrationapi.service;

import github.eojinkim1.registrationapi.controller.dto.request.UserUpdateRequest;
import github.eojinkim1.registrationapi.controller.dto.response.ProfileResponse;
import github.eojinkim1.registrationapi.controller.dto.response.UserResponse;
import github.eojinkim1.registrationapi.domain.Follow;
import github.eojinkim1.registrationapi.repository.FollowRepository;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.repository.UserRepository;
import github.eojinkim1.registrationapi.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("대상 사용자가 존재하지 않습니다."));
    }

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

    public ProfileResponse getProfile(String currentUserEmail, String targetUsername) {
        User target = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        boolean following = false;

        if (currentUserEmail != null) {
            User current = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("현재 사용자를 찾을 수 없습니다."));
            following = followRepository.existsByFollowerAndFollowing(current, target);
        }

        return new ProfileResponse(
                new ProfileResponse.Profile(
                        target.getUsername(),
                        target.getBio(),
                        target.getImage(),
                        following
                )
        );
    }

    @Transactional
    public ProfileResponse followUser(String currentEmail, String targetUsername) {
        User currentUser = findByEmail(currentEmail);
        User targetUser = findByUsername(targetUsername);

        if (!followRepository.existsByFollowerAndFollowing(currentUser, targetUser)) {
            followRepository.save(Follow.of(currentUser, targetUser));
        }
        return buildProfileResponse(targetUser, true);
    }

    @Transactional
    public ProfileResponse unfollowUser(String currentEmail, String targetUsername) {
        User currentUser = findByEmail(currentEmail);
        User targetUser = findByUsername(targetUsername);

        followRepository.deleteByFollowerAndFollowing(currentUser, targetUser);
        return buildProfileResponse(targetUser, false);
    }

    private ProfileResponse buildProfileResponse(User target, boolean following) {
        return new ProfileResponse(new ProfileResponse.Profile(
                target.getUsername(),
                target.getBio(),
                target.getImage(),
                following
        ));
    }
}
