package com.pair.website.domain;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_language_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pair_id")
    private PairBoard pairBoard;

    @Column
    private Boolean cLanguage;

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

    public void update(Boolean cLanguage, Boolean cSharp,
                       Boolean cPlusPlus, Boolean javaScript, Boolean java, Boolean python, Boolean nodeJs,
                       Boolean typeScript) {
        this.cLanguage = cLanguage;
        this.cSharp = cSharp;
        this.cPlusPlus = cPlusPlus;
        this.javaScript = javaScript;
        this.java = java;
        this.python = python;
        this.nodeJs = nodeJs;
        this.typeScript = typeScript;
    }
}
