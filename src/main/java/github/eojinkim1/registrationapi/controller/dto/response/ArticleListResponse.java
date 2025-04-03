package github.eojinkim1.registrationapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ArticleListResponse(
        @JsonProperty("articles") List<ArticleResponse> articles,
        @JsonProperty("articlesCount") long articlesCount
) {
}
