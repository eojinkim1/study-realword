package github.eojinkim1.registrationapi.controller;

import github.eojinkim1.registrationapi.controller.dto.request.ArticleWrapperRequest;
import github.eojinkim1.registrationapi.controller.dto.request.CommentRequest;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleListResponse;
import github.eojinkim1.registrationapi.controller.dto.response.ArticleWrapperResponse;
import github.eojinkim1.registrationapi.controller.dto.response.CommentResponse;
import github.eojinkim1.registrationapi.controller.dto.response.CommentsResponse;
import github.eojinkim1.registrationapi.security.JwtUtil;
import github.eojinkim1.registrationapi.service.ArticleService;
import github.eojinkim1.registrationapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final JwtUtil jwtUtil;
    private final CommentService commentService;

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

    @GetMapping("/api/articles/feed")
    public ArticleListResponse getFeedArticles(
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        String viewerEmail = jwtUtil.validateToken(authHeader.substring(6));
        return articleService.getFeedArticles(limit, offset, viewerEmail);
    }

    @GetMapping("/api/articles/{slug}/comments")
    public CommentsResponse listComments(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String viewerEmail = null;
        if (authHeader != null && authHeader.startsWith("Token ")) {
            viewerEmail = jwtUtil.validateToken(authHeader.substring(6));
        }
        List<CommentResponse> comments = commentService.listComments(slug, viewerEmail);
        return new CommentsResponse(comments);
    }

    @PostMapping("/api/articles")
    public ArticleWrapperResponse createArticle(
            @RequestBody ArticleWrapperRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.validateToken(authHeader.substring(6));
        return articleService.createArticle(request.article(), email);
    }

    @PostMapping("/api/articles/{slug}/comments")
    public CommentResponse addComment(
            @PathVariable("slug") String slug,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.validateToken(authHeader.substring(6));
        return commentService.addComment(slug, request, email);
    }

    @DeleteMapping("/api/articles/{slug}/comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(
            @PathVariable("slug") String slug,
            @PathVariable("id") Long commentId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.validateToken(authHeader.substring(6));
        commentService.deleteComment(slug, commentId, email);
    }
}
