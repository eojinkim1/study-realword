package github.eojinkim1.registrationapi.service;

import github.eojinkim1.registrationapi.controller.dto.request.CommentRequest;
import github.eojinkim1.registrationapi.controller.dto.response.CommentResponse;
import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.Comment;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.repository.ArticleRepository;
import github.eojinkim1.registrationapi.repository.CommentRepository;
import github.eojinkim1.registrationapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentResponse addComment(String slug, CommentRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .body(request.body())
                .author(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponse.from(saved, user);
    }

    public List<CommentResponse> listComments(String slug, String viewerEmail) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        List<Comment> comments = commentRepository.findByArticleOrderByCreatedAtAsc(article);

        return comments.stream()
                .map(c -> CommentResponse.from(c, c.getAuthor()))
                .toList();
    }

    @Transactional
    public void deleteComment(String slug, Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        if (!comment.getArticle().getSlug().equals(slug)) {
            throw new RuntimeException("잘못된 댓글 요청입니다.");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}
