package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    List<ArticleTag> findByArticle(Article article);
}
