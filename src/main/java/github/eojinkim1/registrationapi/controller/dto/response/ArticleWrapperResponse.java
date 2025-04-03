package github.eojinkim1.registrationapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArticleWrapperResponse(
        @JsonProperty("article") ArticleResponse article
) {}
