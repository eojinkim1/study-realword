package github.eojinkim1.registrationapi.controller;

import github.eojinkim1.registrationapi.controller.dto.request.ArticleWrapperRequest;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleListResponse;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleWrapperResponse;
import github.eojinkim1.registrationapi.security.JwtUtil;
import github.eojinkim1.registrationapi.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/articles")
    public ArticleListResponse listArticles(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "favorited", required = false) String favorited,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String viewerEmail = null;
        if (authHeader != null && authHeader.startsWith("Token ")) {
            viewerEmail = jwtUtil.validateToken(authHeader.substring(6));
        }

        return articleService.listArticles(tag, author, favorited, limit, offset, viewerEmail);
    }

    @GetMapping("/api/articles/{slug}")
    public ArticleWrapperResponse getArticle(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String viewerEmail = null;
        if (authHeader != null && authHeader.startsWith("Token ")) {
            viewerEmail = jwtUtil.validateToken(authHeader.substring(6));
        }
        return articleService.getArticleBySlug(slug, viewerEmail);
    }

    @PostMapping("/api/articles")
    public ArticleWrapperResponse createArticle(
            @RequestBody ArticleWrapperRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.validateToken(authHeader.substring(6));
        return articleService.createArticle(request.article(), email);
    }
}
