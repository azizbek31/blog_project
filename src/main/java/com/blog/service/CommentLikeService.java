package com.blog.service;

import com.blog.dto.response.LikeResponse;
import com.blog.exception.ResourceNotFoundException;
import com.blog.model.Comment;
import com.blog.model.CommentLike;
import com.blog.model.User;
import com.blog.repository.CommentLikeRepository;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public LikeResponse toggleLike(Long postId, Long commentId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Comment not found");
        }
        boolean isLiked;
        if (commentLikeRepository.existsByUserIdAndCommentId(user.getId(), comment.getId())) {
            commentLikeRepository.deleteByUserIdAndCommentId(user.getId(), comment.getId());
            isLiked = false;
        } else {
            CommentLike commentLike = new CommentLike();
            commentLike.setUser(user);
            commentLike.setComment(comment);
            commentLikeRepository.save(commentLike);
            isLiked = true;
        }
        long count = commentLikeRepository.countByCommentId(comment.getId());
        return new LikeResponse(isLiked, count);
    }


}
