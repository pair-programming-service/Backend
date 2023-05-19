package com.pair.website.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Setter;

@Builder
@Getter
public class BoardAllResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String ide;
    private String runningTime;
    private String proceed;
    private String category;
    private List<String> language;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd", timezone = "Asia/Seoul", locale = "en")
    // 시간정보 형식 변경
    private LocalDate runningDate;
    private Boolean status;
    private int viewCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd HH:MM", timezone = "Asia/Seoul", locale = "en")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd HH:MM", timezone = "Asia/Seoul", locale = "en")
    private LocalDateTime updatedAt;
}
