package com.pair.website.dto;

import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.PairBoard;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Long boardLanguageId;
    private String title;
    private String content;
    private String ide;
    private String proceed;
    private String runningTime;
    private String category;
    private LocalDate runningDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;
    private int viewCount;

    private Boolean cLanguage;
    private Boolean cSharp;
    private Boolean cPlusPlus;
    private Boolean javaScript;
    private Boolean java;
    private Boolean python;
    private Boolean nodeJs;
    private Boolean typeScript;


}
