package github.eojinkim1.registrationapi.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArticleWrapperRequest(
        @JsonProperty("article") ArticleRequest article
) {
}
