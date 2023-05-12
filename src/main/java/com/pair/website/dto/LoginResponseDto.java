package com.pair.website.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.pair.website.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginResponseDto {
    private boolean loginSuccess;
    private Long id;
    private String nickname;
    private String profileImage;
    private String githubLink;
    private LocalDateTime createdAt;

}
