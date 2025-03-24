package github.eojinkim1.registrationapi.controller;

import github.eojinkim1.registrationapi.controller.dto.request.LoginRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserUpdateRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserWrapper;
import github.eojinkim1.registrationapi.controller.dto.response.LoginResponse;
import github.eojinkim1.registrationapi.controller.dto.response.ProfileResponse;
import github.eojinkim1.registrationapi.controller.dto.response.UserResponse;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.security.JwtUtil;
import github.eojinkim1.registrationapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/users")
    public UserResponse registration(@RequestBody UserWrapper<UserRequest> request) {
        User user = userService.registration(
                request.getUser().email(),
                request.getUser().password(),
                request.getUser().username()
        );
        return new UserResponse(
                user.getEmail(),
                null,
                user.getUsername(),
                user.getBio(),
                user.getImage()
        );
    }

    @PostMapping("/api/users/login")
    public LoginResponse login(@RequestBody UserWrapper<LoginRequest> loginRequest) {
        UserResponse userResponse = userService.login(
                loginRequest.getUser().email(),
                loginRequest.getUser().password()
        );

        System.out.println("Login Response JSON: " + userResponse);


        return new LoginResponse(userResponse);
    }

    @GetMapping("/api/user")
    public ResponseEntity<LoginResponse> getCurrentUser(@RequestHeader("Authorization")String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Token ")) {
            throw new RuntimeException("유효한 토큰이 필요합니다.");
        }
        String token = authHeader.substring(6); // 토큰 제거
        String email = jwtUtil.validateToken(token);

        UserResponse userResponse = userService.getCurrentUser(email, token);

        return ResponseEntity.ok(new LoginResponse(userResponse));
    }

    @GetMapping("/api/profiles/{username}")
    public ProfileResponse getProfile(@PathVariable("username") String username) {
        return userService.getProfile(username);
    }

    @PutMapping("/api/user")
    public ResponseEntity<LoginResponse> updateUser(
            @RequestHeader("Authorization")String authHeader,
            @RequestBody UserWrapper<UserUpdateRequest> request) {

        if(authHeader == null || authHeader.isBlank()) {
            throw new RuntimeException("요청 헤더에 Authorization을 추가하여 유효한 토큰을 입력해주세요.");
        }
        if (!authHeader.startsWith("Token ")) {
            throw new RuntimeException("Authorization 형식이 잘못되었습니다. 'Token <JWT>' 형식이어야 합니다.");
        }

        String token = authHeader.substring(6);
        String email = jwtUtil.validateToken(token);
        UserResponse userResponse = userService.updateUser(email, request.getUser());

        return ResponseEntity.ok(new LoginResponse(userResponse));
    }
}