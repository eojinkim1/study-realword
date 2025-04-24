package github.eojinkim1.registrationapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CommentsResponse(
        @JsonProperty("comments")
        List<CommentResponse> comments
) {}
