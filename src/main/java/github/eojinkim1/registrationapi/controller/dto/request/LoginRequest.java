package github.eojinkim1.registrationapi.controller.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
