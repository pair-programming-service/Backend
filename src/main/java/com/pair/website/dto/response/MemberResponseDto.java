package com.pair.website.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private String githubLink;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YY-MM-dd HH:MM", timezone = "Asia/Seoul", locale = "en")
    private LocalDateTime createdAt;
}
