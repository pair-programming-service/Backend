package com.pair.website.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PairBoardSaveRequestDto {

    private String title;
    private String content;
    private String ide;
    private String proceed;
    private String runningTime;
    private String category;
    private Boolean status;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate runningDate;
    private List<String> language;

    // dto -> entity
    // status null 값 default true 로 하기
    public PairBoard toEntity(Member member) {
        return PairBoard.builder()
            .member(member).title(title).content(content).ide(ide).proceed(proceed).runningTime(runningTime)
            .category(category).runningDate(runningDate).status(true).build();

    }
}
