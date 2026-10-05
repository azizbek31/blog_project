package com.blog.dto.response;

import com.blog.model.Post;
import com.blog.model.PostImage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private List<String> images;

    public PostResponse(Post post) {
        if (post != null) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.author = post.getAuthor().getUsername();
            this.createdDate = post.getCreatedDate();
            this.images = post.getImages().stream().map(PostImage::getObjectKey).toList();
        }
    }
}
