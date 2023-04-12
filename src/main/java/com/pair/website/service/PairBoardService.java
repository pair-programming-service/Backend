package com.pair.website.service;

import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.PairBoardSaveResponseDto;
import com.pair.website.dto.response.BoardAllResponseDto;
import com.pair.website.repository.BoardLanguageRepository;
import com.pair.website.repository.PairBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PairBoardService {

    private final PairBoardRepository pairBoardRepository;
    private final BoardLanguageRepository boardLanguageRepository;

    @Transactional
    public BaseResponseDto<?> save(PairBoardSaveRequestDto requestDto) {

        PairBoard pairBoard = requestDto.toEntity();
        pairBoardRepository.save(pairBoard);

        BoardLanguage boardLanguage = BoardLanguage.builder().pairBoard(pairBoard)
            .cLanguage(requestDto.getCLanguage()).cSharp(requestDto.getCSharp())
            .cPlusPlus(requestDto.getCPlusPlus()).javaScript(requestDto.getJavaScript())
            .java(requestDto.getJava()).python(requestDto.getPython())
            .nodeJs(requestDto.getNodeJs()).typeScript(requestDto.getTypeScript()).build();
        log.info("boardLanguage : {}",boardLanguage);
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
    @Transactional(readOnly = true)
    public BaseResponseDto<?> getAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size); // 페이징 처리

        List<PairBoard> pairBoards = pairBoardRepository.findAllBySearch(pageable,keyword);
        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();
        for (PairBoard pairBoard : pairBoards) {
            // BoardLanguage정보를 Response에 담아주기 위한 객체 생성
            Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findById(pairBoard.getBoardLanguage().getId());
            List<String> languageList = new ArrayList<>();
            languageCheck(boardLanguage,languageList);
            boardAllResponseDtos.add(BoardAllResponseDto.builder()
                    .id(pairBoard.getId())
                    .title(pairBoard.getTitle())
                    .content(pairBoard.getContent())
                    .ide(pairBoard.getIde())
                    .runningTime(pairBoard.getRunningTime())
                    .proceed(pairBoard.getProceed())
                    .category(pairBoard.getCategory())
                    .language(languageList)
                    .runningDate(pairBoard.getRunningDate())
                    .status(pairBoard.getStatus())
                    .viewCount(pairBoard.getViewCount())
                    .createdAt(pairBoard.getCreatedAt())
                    .updatedAt(pairBoard.getUpdatedAt())
                    .build()
            );

        }

        return BaseResponseDto.success(boardAllResponseDtos);
    }

    public BaseResponseDto<?> update(PairBoardSaveRequestDto requestDto, Long id) {

        PairBoard pairBoard = pairBoardRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);

        BoardLanguage boardLanguage = boardLanguageRepository.findById(
            pairBoard.getBoardLanguage().getId()).orElseThrow(IllegalArgumentException::new);
        boardLanguage.update(requestDto.getCLanguage(), requestDto.getCSharp(),
            requestDto.getCPlusPlus(), requestDto.getJavaScript(), requestDto.getJava()
            , requestDto.getPython(), requestDto.getNodeJs(), requestDto.getTypeScript());

        pairBoard.update(boardLanguage, requestDto.getTitle(), requestDto.getContent(),
            requestDto.getIde(),
            requestDto.getProceed(),
            requestDto.getRunningTime(), requestDto.getCategory(),
            requestDto.getRunningDate(), pairBoard.getStatus(),
            pairBoard.getViewCount());

        PairBoardSaveResponseDto responseDto = PairBoardSaveResponseDto.builder().id(
                pairBoard.getId()).boardLanguageId(boardLanguage.getId()).title(pairBoard.getTitle())
            .content(
                pairBoard.getContent()).ide(pairBoard.getIde()).proceed(pairBoard.getProceed())
            .runningTime(
                pairBoard.getRunningTime()).category(pairBoard.getCategory())
            .runningDate(pairBoard.getRunningDate()).createdAt(pairBoard.getCreatedAt())
            .updatedAt(pairBoard.getUpdatedAt()).status(pairBoard.getStatus()).viewCount(
                pairBoard.getViewCount()).cLanguage(boardLanguage.getCLanguage())
            .cSharp(boardLanguage.getCSharp())
            .cPlusPlus(boardLanguage.getCPlusPlus()).javaScript(boardLanguage.getJavaScript())
            .java(boardLanguage.getJava()).python(boardLanguage.getPython())
            .nodeJs(boardLanguage.getNodeJs()).typeScript(boardLanguage.getTypeScript()).build();

        return BaseResponseDto.success(responseDto);

}
    // 전체 보기 Response에 language를 Boolean 타입이 아닌 스트링 형태로 보내 위한 메서드
    public void languageCheck(Optional<BoardLanguage> boardLanguage,List<String> languageList) {
        if(boardLanguage.get().getCLanguage() == Boolean.TRUE) languageList.add("cLanguage");
        if(boardLanguage.get().getCSharp() == Boolean.TRUE) languageList.add("cSharp");
        if(boardLanguage.get().getCPlusPlus() == Boolean.TRUE) languageList.add("cPlusPlus");
        if(boardLanguage.get().getJavaScript() == Boolean.TRUE) languageList.add("javaScript");
        if(boardLanguage.get().getJava() == Boolean.TRUE) languageList.add("java");
        if(boardLanguage.get().getPython() == Boolean.TRUE) languageList.add("python");
        if(boardLanguage.get().getNodeJs() == Boolean.TRUE) languageList.add("nodeJs");
        if(boardLanguage.get().getTypeScript() == Boolean.TRUE) languageList.add("typeScript");
    }
}
