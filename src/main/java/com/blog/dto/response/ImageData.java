package com.blog.dto.response;

import org.springframework.core.io.Resource;

public record ImageData(Resource resource, String contentType) {
}
