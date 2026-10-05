package com.blog.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private boolean liked;
    private long totalLikes;
}
