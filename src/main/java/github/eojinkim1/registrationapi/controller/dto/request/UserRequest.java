package github.eojinkim1.registrationapi.controller.dto.request;

public record UserRequest(
        String username,
        String email,
        String password
) {
}

