package com.pair.website.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileRequestDto {
    private String nickname;
    private String profileImage;
    private String githubLink;
}
