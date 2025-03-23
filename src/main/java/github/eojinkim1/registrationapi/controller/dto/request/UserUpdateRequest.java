package github.eojinkim1.registrationapi.controller.dto.request;

public record UserUpdateRequest(
        String username,
        String password,
        String bio,
        String image
) {
}
