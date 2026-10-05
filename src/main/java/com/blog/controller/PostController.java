package com.blog.controller;

import com.blog.dto.request.PostRequest;
import com.blog.dto.response.ImageData;
import com.blog.dto.response.LikeResponse;
import com.blog.dto.response.PageResponse;
import com.blog.dto.response.PostResponse;
import com.blog.service.PostImageService;
import com.blog.service.PostLikeService;
import com.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final PostImageService postImageService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        String username = authentication.getName();
        PostResponse post = postService.createPost(postRequest, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PostResponse>> getAllPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<PostResponse>> getPostsByTitle(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(postService.searchByTitle(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id,
                                                   @Valid @RequestBody PostRequest postRequest,
                                                   Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(postService.updatePost(id, postRequest, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        postService.deletePost(id, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponse> toggleLike(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(postLikeService.toggleLike(id, username));
    }

    @PostMapping("/{postId}/images")
    public ResponseEntity<String> uploadImage(@PathVariable Long postId,
                                              @RequestParam("file") MultipartFile file,
                                              Authentication authentication) {
        String result = postImageService.addImage(postId, file, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{postId}/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable Long postId, @PathVariable String filename) {
        ImageData imageData = postImageService.loadImage(postId, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageData.contentType()))
                .body(imageData.resource());
    }
}
