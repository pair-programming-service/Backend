package com.pair.website.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.Member;
import com.pair.website.domain.PairBoard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PairBoardSaveResponseDto {

    private Long id;
    private Long member;
    private Long boardLanguageId;
    private String title;
    private String content;
    private String ide;
    private String proceed;
    private String runningTime;
    private String category;
    private LocalDate runningDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd HH:MM", timezone = "Asia/Seoul", locale = "en")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd HH:MM", timezone = "Asia/Seoul", locale = "en")
    private LocalDateTime updatedAt;
    private Boolean status;
    private int viewCount;

    private List<String> language;


}
