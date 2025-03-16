package github.eojinkim1.registrationapi.controller;

import github.eojinkim1.registrationapi.controller.dto.request.LoginRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserWrapper;
import github.eojinkim1.registrationapi.controller.dto.response.LoginResponse;
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
}