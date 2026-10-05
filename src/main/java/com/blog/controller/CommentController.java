package com.blog.controller;

import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;
import com.blog.dto.response.LikeResponse;
import com.blog.dto.response.PageResponse;
import com.blog.service.CommentLikeService;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId,
                                                         @Valid @RequestBody CommentRequest commentRequest,
                                                         Authentication authentication) {
        CommentResponse commentResponse = commentService.addComment(postId, commentRequest, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CommentResponse>> getAllComments(@PathVariable Long postId, Pageable pageable) {
        PageResponse<CommentResponse> comments = commentService.getCommentsByPostId(postId, pageable);
        return ResponseEntity.ok(comments);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              Authentication authentication) {
        commentService.deleteComment(postId, commentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<LikeResponse> toggleLike(@PathVariable Long postId,
                                                   @PathVariable Long commentId,
                                                   Authentication authentication) {
        LikeResponse likeResponse = commentLikeService.toggleLike(postId, commentId, authentication.getName());
        return ResponseEntity.ok(likeResponse);
    }

}
