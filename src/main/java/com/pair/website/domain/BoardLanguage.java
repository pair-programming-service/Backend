package com.pair.website.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLanguage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_language_id")
    private Long id;

    @OneToOne(mappedBy = "boardLanguage", fetch = FetchType.LAZY)
    private PairBoard pairBoard;

    @Column
    private Boolean C;

    @Column
    private Boolean cSharp;

    @Column
    private Boolean cPlusPlus;

    @Column
    private Boolean javaScript;

    @Column
    private Boolean java;

    @Column
    private Boolean python;

    @Column
    private Boolean nodeJs;

    @Column
    private Boolean typeScript;


}
