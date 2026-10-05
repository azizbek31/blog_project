package com.blog.service;

import com.blog.dto.request.PostRequest;
import com.blog.dto.response.PageResponse;
import com.blog.dto.response.PostResponse;
import com.blog.exception.ResourceNotFoundException;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest postRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found"));
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(user);
        Post save = postRepository.save(post);
        return new PostResponse(save);
    }

    public PageResponse<PostResponse> getAllPosts(Pageable pageable) {
        Page<PostResponse> page = postRepository.findAll(pageable).map(PostResponse::new);
        return PageResponse.from(page);
    }

    @Cacheable(value = "posts", key = "#id")
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return new PostResponse(post);
    }

    public PageResponse<PostResponse> searchByTitle(String keyword, Pageable pageable) {
        Page<PostResponse> page = postRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(PostResponse::new);
        return PageResponse.from(page);
    }

    @CacheEvict(value = "posts", key = "#id")
    public PostResponse updatePost(Long id, PostRequest postRequest, String username) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        Post save = postRepository.save(post);
        return new PostResponse(save);
    }

    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
        postRepository.deleteById(id);
    }
}
