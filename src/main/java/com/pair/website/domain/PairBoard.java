package com.pair.website.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "title_idx", columnList = "title"),
                  @Index(name = "content_idx", columnList = "content"),
                  @Index(name = "category_idx", columnList = "category")})
public class PairBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pair_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "pairBoard", fetch = FetchType.LAZY)
    private BoardLanguage boardLanguage;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String ide;

    @Column
    private String runningTime;

    @Column
    private String category;

    @Column
    private String proceed;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate runningDate;

    @Column // default를 어떻게 true로 할까? 지금 builder에 True로 해놨음
    // @ColumnDefault() //: 사용법 알아보기  // 1: 모집중, 0: 모집완료
    private Boolean status;

    @Column
    private int viewCount;

    public void update(BoardLanguage boardLanguage, String title,
        String content,
        String ide, String category, String runningTime, String proceed, LocalDate runningDate,
        Boolean status, int viewCount) {
        this.boardLanguage = boardLanguage;
        this.title = title;
        this.content = content;
        this.ide = ide;
        this.category = category;
        this.runningTime = runningTime;
        this.proceed = proceed;
        this.runningDate = runningDate;
        this.status = status;
        this.viewCount = viewCount;
    }

}
