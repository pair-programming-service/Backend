package com.pair.website.service;

import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.PairBoardSaveResponseDto;
import com.pair.website.repository.BoardLanguageRepository;
import com.pair.website.repository.PairBoardRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PairBoardService {


    private final PairBoardRepository pairBoardRepository;
    private final BoardLanguageRepository boardLanguageRepository;

    public BaseResponseDto<?> save(PairBoardSaveRequestDto requestDto) {

        PairBoard pairBoard = requestDto.toEntity();
        pairBoardRepository.save(pairBoard);

        BoardLanguage boardLanguage = BoardLanguage.builder().pairBoard(pairBoard)
            .cLanguage(requestDto.getCLanguage()).cSharp(requestDto.getCSharp())
            .cPlusPlus(requestDto.getCPlusPlus()).javaScript(requestDto.getJavaScript())
            .java(requestDto.getJava()).python(requestDto.getPython())
            .nodeJs(requestDto.getNodeJs()).typeScript(requestDto.getTypeScript()).build();

        boardLanguageRepository.save(boardLanguage);

        PairBoardSaveResponseDto responseDto = PairBoardSaveResponseDto.builder().id(
                pairBoard.getId()).boardLanguageId(boardLanguage.getId()).title(pairBoard.getTitle())
            .content(
                pairBoard.getContent()).ide(pairBoard.getIde()).proceed(pairBoard.getProceed())
            .runningTime(
                pairBoard.getRunningTime()).category(pairBoard.getCategory())
            .runningDate(pairBoard.getRunningDate()).createdAt(pairBoard.getCreatedAt())
            .updatedAt(pairBoard.getUpdatedAt()).status(pairBoard.getStatus()).viewCount(
                pairBoard.getViewCount()).cLanguage(requestDto.getCLanguage())
            .cSharp(requestDto.getCSharp())
            .cPlusPlus(requestDto.getCPlusPlus()).javaScript(requestDto.getJavaScript())
            .java(requestDto.getJava()).python(requestDto.getPython())
            .nodeJs(requestDto.getNodeJs()).typeScript(requestDto.getTypeScript()).build();

        return BaseResponseDto.success(responseDto);
    }


}
