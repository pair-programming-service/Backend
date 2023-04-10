package com.pair.website.dto;

import com.pair.website.domain.PairBoard;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PairBoardSaveResponseDto {

    private Long id;
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

    @Builder
    public PairBoardSaveResponseDto(PairBoard entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.ide = entity.getIde();
        this.proceed = entity.getProceed();
        this.runningTime = entity.getRunningTime();
        this.category = entity.getCategory();
        this.runningDate = entity.getRunningDate();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.status = entity.getStatus();
        this.viewCount = entity.getViewCount();
    }
}
