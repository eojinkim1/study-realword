package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글에 달린 댓글을 시간 순으로 조회
    List<Comment> findByArticleOrderByCreatedAtAsc(Article article);
}
