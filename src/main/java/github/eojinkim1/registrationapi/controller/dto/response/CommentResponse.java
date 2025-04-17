package github.eojinkim1.registrationapi.controller.dto.response;

import github.eojinkim1.registrationapi.domain.Comment;
import github.eojinkim1.registrationapi.domain.User;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String body,
        AuthorProfileResponse author
) {
    public static CommentResponse from(Comment comment, User author) {
        return new CommentResponse(
                comment.getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getBody(),
                AuthorProfileResponse.from(author, false)
        );
    }
}
