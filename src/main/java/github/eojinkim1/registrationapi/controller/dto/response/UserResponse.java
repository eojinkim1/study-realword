package github.eojinkim1.registrationapi.controller.dto.response;

public record UserResponse(
        String email,
        String token,
        String username,
        String bio,
        String image
){
}