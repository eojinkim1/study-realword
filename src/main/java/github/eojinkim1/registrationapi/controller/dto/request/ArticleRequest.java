package github.eojinkim1.registrationapi.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ArticleRequest(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("body") String body,
        @JsonProperty("tagList") List<String> tagList
) {
}
