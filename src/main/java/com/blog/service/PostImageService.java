package com.blog.service;

import com.blog.dto.response.ImageData;
import com.blog.exception.ResourceNotFoundException;
import com.blog.model.Post;
import com.blog.model.PostImage;
import com.blog.repository.PostImageRepository;
import com.blog.repository.PostRepository;
import com.blog.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;
    private final PostImageRepository postImageRepository;
    @Value("${app.storage.local-path}")
    private String basePath;

    public String addImage(Long postId, MultipartFile file, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
        String objectKey = fileStorageService.upload(file, "posts/" + postId);

        PostImage image = new PostImage();
        image.setObjectKey(objectKey);
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSizeInBytes(file.getSize());
        post.addImage(image);
        postRepository.save(post);
        return objectKey;
    }

    public ImageData loadImage(Long postId, String filename) {
        String objectKey = "posts/" + postId + "/" + filename;
        PostImage image = postImageRepository.findByObjectKey(objectKey).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        Resource resource = fileStorageService.load(objectKey);
        return new ImageData(resource, image.getContentType());
    }

}
