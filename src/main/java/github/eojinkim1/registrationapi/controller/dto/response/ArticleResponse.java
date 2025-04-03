package github.eojinkim1.registrationapi.controller.dto.response;

import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.repository.ArticleTagRepository;
import github.eojinkim1.registrationapi.repository.FavoriteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ArticleResponse(
        String slug,
        String title,
        String description,
        String body,
        List<String> tagList,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean favorited,
        int favoritesCount,
        ProfileResponse.Profile author
) {
    public static ArticleResponse from(
            Article article,
            User viewer,
            FavoriteRepository favoriteRepo,
            ArticleTagRepository tagRepo
    ) {
        boolean favorited = viewer != null && favoriteRepo.existsByUserAndArticle(viewer, article);

        List<String> tagList = tagRepo.findByArticle(article).stream()
                .map(at -> at.getTag().getName())
                .collect(Collectors.toList());

        return new ArticleResponse(
                article.getSlug(),
                article.getTitle(),
                article.getDescription(),
                article.getBody(),
                tagList,
                article.getCreatedAt(),
                article.getUpdatedAt(),
                favorited,
                (int) favoriteRepo.countByArticle(article),
                new ProfileResponse.Profile(
                        article.getAuthor().getUsername(),
                        article.getAuthor().getBio(),
                        article.getAuthor().getImage(),
                        false
                )
        );
    }
}
