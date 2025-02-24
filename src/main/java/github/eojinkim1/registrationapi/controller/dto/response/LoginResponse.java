package github.eojinkim1.registrationapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("user") UserResponse user
) {}
