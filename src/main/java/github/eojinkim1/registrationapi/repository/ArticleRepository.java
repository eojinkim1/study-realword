package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    Optional<Article> findBySlug(String slug);
    int countBySlug(String slug);

    Page<Article> findByAuthorIn(List<User> authors, Pageable pageable);
}
