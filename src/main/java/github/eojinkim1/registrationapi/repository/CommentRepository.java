package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
