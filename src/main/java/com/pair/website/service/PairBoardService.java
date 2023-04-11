package com.pair.website.service;

import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.PairBoardSaveResponseDto;
import com.pair.website.dto.response.BoardAllResponseDto;
import com.pair.website.dto.response.BoardLanguageResponseDto;
import com.pair.website.repository.BoardLanguageRepository;
import com.pair.website.repository.PairBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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

    // 페어 목록 글 전체 보기
    public BaseResponseDto<?> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 페이징 처리

        List<PairBoard> pairBoards = pairBoardRepository.findAllByOrderByIdDesc(
            pageable); // id기준으로 내림차순 정렬

        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();
        for (PairBoard pairBoard : pairBoards) {
            // BoardLanguage정보를 Response에 담아주기 위한 객체 생성
            Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findById(
                pairBoard.getBoardLanguage().getId());


        }

        return BaseResponseDto.success(boardAllResponseDtos);
    }

}
