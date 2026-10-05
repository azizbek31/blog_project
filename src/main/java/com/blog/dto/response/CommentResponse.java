package com.blog.dto.response;

import com.blog.model.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private Long postId;
    private LocalDateTime createdDate;

    public CommentResponse(Comment comment) {
        if (comment != null) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.author = comment.getAuthor().getUsername();
            this.postId = comment.getPost().getId();
            this.createdDate = comment.getCreatedDate();
        }
    }
}
