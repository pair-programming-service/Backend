package com.pair.website.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
public class BoardAllResponseDto {
    private Long id;
    private String title;
    private String content;
    private String ide;
    private String runningTime;
    private String proceed;
    private BoardLanguageResponseDto boardLanguage;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="YY-MM-dd HH:MM", timezone = "Asia/Seoul",  locale = "en") // 시간정보 형식 변경
    private LocalDateTime runningDate;
    private Boolean status;
    private int viewCount;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="YY-MM-dd HH:MM", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="YY-MM-dd HH:MM", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime updatedAt;
}
