package com.pair.website.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public PairBoard(Long id, Member member, BoardLanguage boardLanguage, String title,
        String content,
        String ide, String runningTime, String category, String proceed, LocalDate runningDate,
        Boolean status, int viewCount) {
        this.id = id;
        this.member = member;
        this.boardLanguage = boardLanguage;
        this.title = title;
        this.content = content;
        this.ide = ide;
        this.runningTime = runningTime;
        this.category = category;
        this.proceed = proceed;
        this.runningDate = runningDate;
        this.status = status;
        this.viewCount = viewCount;
    }

}
