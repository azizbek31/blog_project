package com.blog.service;

import com.blog.config.RabbitMQConfig;
import com.blog.dto.message.CommentNotificationMessage;
import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;
import com.blog.dto.response.PageResponse;
import com.blog.exception.ResourceNotFoundException;
import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public CommentResponse addComment(Long postId, CommentRequest commentRequest, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setAuthor(user);
        Comment save = commentRepository.save(comment);
        if (!post.getAuthor().getUsername().equals(username)) {
            CommentNotificationMessage message = new CommentNotificationMessage(
                    post.getId(), save.getId(), username, post.getAuthor().getId()
            );
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.COMMENT_EXCHANGE,
                    RabbitMQConfig.COMMENT_ROUTING_KEY,
                    message
            );
        }
        return new CommentResponse(save);
    }

    public PageResponse<CommentResponse> getCommentsByPostId(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        Page<CommentResponse> page = commentRepository.findByPostId(postId, pageable).map(CommentResponse::new);
        return PageResponse.from(page);
    }

    public void deleteComment(Long postId, Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Comment does not belong to this post");
        }
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
        commentRepository.deleteById(commentId);
    }

}
