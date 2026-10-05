package com.blog.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends Auditable{

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    public void addImage(PostImage image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(PostImage image) {
        images.remove(image);
        image.setPost(null);
    }

    public List<PostImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

}
