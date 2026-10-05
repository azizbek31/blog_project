package com.blog.service;

import com.blog.dto.response.LikeResponse;
import com.blog.exception.ResourceNotFoundException;
import com.blog.model.Post;
import com.blog.model.PostLike;
import com.blog.model.User;
import com.blog.repository.PostLikeRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeResponse toggleLike(Long postId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Username not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        boolean isLiked;
        if (postLikeRepository.existsByUserIdAndPostId(user.getId(), post.getId())) {
            postLikeRepository.deleteByUserIdAndPostId(user.getId(), post.getId());
            isLiked = false;
        } else {
            PostLike postLike = new PostLike();
            postLike.setUser(user);
            postLike.setPost(post);
            postLikeRepository.save(postLike);
            isLiked = true;
        }
        long count = postLikeRepository.countByPostId(postId);
        return new LikeResponse(isLiked, count);
    }

}
