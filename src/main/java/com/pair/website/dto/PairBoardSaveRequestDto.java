package com.pair.website.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PairBoardSaveRequestDto {

    private String title;
    private String content;
    private String ide;
    private String proceed;
    private String runningTime;
    private String category;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate runningDate;

    private Boolean cLanguage;
    private Boolean cSharp;
    private Boolean cPlusPlus;
    private Boolean javaScript;
    private Boolean java;
    private Boolean python;
    private Boolean nodeJs;
    private Boolean typeScript;

    // dto -> entity
    public PairBoard toEntity() {
        return PairBoard.builder()
            .title(title).content(content).ide(ide).proceed(proceed).runningTime(runningTime)
            .category(category).runningDate(runningDate).status(true).build();
    }
}
