package com.pair.website.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.pair.website.domain.PairBoard;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    // dto -> entity
    @Builder
    public PairBoardSaveRequestDto(String title, String content, String ide, String proceed,
        String runningTime, String category, LocalDate runningDate) {
        this.title = title;
        this.content = content;
        this.ide = ide;
        this.proceed = proceed;
        this.runningTime = runningTime;
        this.category = category;
        this.runningDate = runningDate;
    }

    public PairBoard toEntity() {
        return PairBoard.builder()
            .title(title).content(content).ide(ide).proceed(proceed).runningTime(runningTime)
            .category(category).runningDate(runningDate).status(true).build();
    }
}
