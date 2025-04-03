package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.Favorite;
import github.eojinkim1.registrationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndArticle(User user, Article article);
    long countByArticle(Article article);
}
