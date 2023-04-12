package com.pair.website.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardLanguageResponseDto {
    private Boolean cLanguage;
    private Boolean cSharp;
    private Boolean cPlusPlus;
    private Boolean javaScript;
    private Boolean java;
    private Boolean python;
    private Boolean nodeJs;
    private Boolean typeScript;

}
