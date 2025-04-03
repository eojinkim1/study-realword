package github.eojinkim1.registrationapi.service;

import github.eojinkim1.registrationapi.controller.ArticleController;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleListResponse;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleResponse;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleWrapperResponse;
import github.eojinkim1.registrationapi.domain.Article;
import github.eojinkim1.registrationapi.domain.User;
import github.eojinkim1.registrationapi.repository.ArticleRepository;
import github.eojinkim1.registrationapi.repository.ArticleTagRepository;
import github.eojinkim1.registrationapi.repository.FavoriteRepository;
import github.eojinkim1.registrationapi.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;
    private final ArticleTagRepository articleTagRepository;
    private final UserRepository userRepository;

    public ArticleListResponse listArticles(
            String tag,
            String author,
            String favorited,
            int limit,
            int offset,
            String viewerEmail
    ) {
        // 페이지 번호 계산 (limit이 0이면 페이지 0으로 고정)
        int page = limit > 0 ? offset / limit : 0;

        // 최신순 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 필터 조건 생성
        Specification<Article> spec = buildSpecification(tag, author, favorited);

        // 게시글 페이지 조회
        Page<Article> pageResult = articleRepository.findAll(spec, pageable);

        // 로그인한 사용자 정보 (좋아요 여부 체크용)
        User viewer;
        if (viewerEmail != null) {
            viewer = userRepository.findByEmail(viewerEmail).orElse(null);
        } else {
            viewer = null;
        }

        List<ArticleResponse> articles = pageResult.getContent().stream()
                .map(article -> ArticleResponse.from(article, viewer, favoriteRepository, articleTagRepository))
                .toList();

        return new ArticleListResponse(articles, pageResult.getTotalElements());
    }

    private Specification<Article> buildSpecification(String tag, String author, String favorited) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tag != null) {
                Join<Object, Object> tagsJoin = root.join("tags", JoinType.INNER);
                predicates.add(cb.equal(tagsJoin.get("tag").get("name"), tag));
            }

            if (author != null) {
                predicates.add(cb.equal(root.get("author").get("username"), author));
            }

            if (favorited != null) {
                Join<Object, Object> favJoin = root.join("favorites", JoinType.LEFT);
                predicates.add(cb.equal(favJoin.get("user").get("username"), favorited));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public ArticleWrapperResponse getArticleBySlug(String slug, String viewerEmail) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("해당 slug의 게시글이 존재하지 않습니다."));

        User viewer = viewerEmail != null ? userRepository.findByEmail(viewerEmail).orElse(null) : null;

        ArticleResponse articleResponse = ArticleResponse.from(article, viewer, favoriteRepository, articleTagRepository);

        return new ArticleWrapperResponse(articleResponse);
    }
}
