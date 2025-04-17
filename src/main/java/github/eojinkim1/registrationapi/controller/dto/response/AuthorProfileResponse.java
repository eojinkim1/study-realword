package github.eojinkim1.registrationapi.controller.dto.response;

import github.eojinkim1.registrationapi.domain.User;

public record AuthorProfileResponse(
        String username,
        String bio,
        String image,
        boolean following
) {
    public static AuthorProfileResponse from(User user, boolean following) {
        return new AuthorProfileResponse(
                user.getUsername(),
                user.getBio(),
                user.getImage(),
                following
        );
    }
}
