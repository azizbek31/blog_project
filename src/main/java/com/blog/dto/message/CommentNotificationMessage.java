package com.blog.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentNotificationMessage {
    private Long postId;
    private Long commentId;
    private String commentAuthor;
    private Long postAuthorId;
}
