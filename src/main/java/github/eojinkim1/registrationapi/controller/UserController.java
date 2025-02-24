package github.eojinkim1.registrationapi.controller;

import github.eojinkim1.registrationapi.controller.dto.request.LoginRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserRequest;
import github.eojinkim1.registrationapi.controller.dto.request.UserWrapper;
import github.eojinkim1.registrationapi.controller.dto.response.LoginResponse;
import github.eojinkim1.registrationapi.controller.dto.response.UserResponse;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/users")
    public UserResponse registration(@RequestBody UserWrapper<UserRequest> request) {
        User user = userService.registration(
                request.getUser().email(),
                request.getUser().password(),
                request.getUser().username()
        );
        System.out.println("회원가입 완료: " + user.getEmail());

        // 저장된 사용자 확인
        userService.printAllUsers();

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
}