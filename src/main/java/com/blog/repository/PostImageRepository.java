package com.blog.repository;

import com.blog.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    Optional<PostImage> findByObjectKey(String objectKey);

}
