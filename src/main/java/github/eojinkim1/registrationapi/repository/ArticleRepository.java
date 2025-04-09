package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    Optional<Article> findBySlug(String slug);
    int countBySlug(String slug);
}
