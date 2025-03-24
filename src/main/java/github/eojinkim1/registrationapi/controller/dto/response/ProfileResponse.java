package github.eojinkim1.registrationapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProfileResponse(
        @JsonProperty("profile")
        Profile profile
) {
    public record Profile(
            String username,
            String bio,
            String image,
            boolean following
    ) {}
}
