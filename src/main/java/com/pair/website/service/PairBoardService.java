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

        List<String> languageList = requestDto.getLanguage();
        BoardLanguageResponseDto boardLanguageResponseDto = getBoardLanguageResponseDto(
            languageList);

        BoardLanguage boardLanguage = BoardLanguage.builder().pairBoard(pairBoard)
            .cLanguage(boardLanguageResponseDto.getCLanguage())
            .cSharp(boardLanguageResponseDto.getCSharp())
            .cPlusPlus(boardLanguageResponseDto.getCPlusPlus())
            .javaScript(boardLanguageResponseDto.getJavaScript())
            .java(boardLanguageResponseDto.getJava()).python(boardLanguageResponseDto.getPython())
            .nodeJs(boardLanguageResponseDto.getNodeJs())
            .typeScript(boardLanguageResponseDto.getTypeScript()).build();

        log.info("boardLanguage : {}", boardLanguage);
        boardLanguageRepository.save(boardLanguage);

        PairBoardSaveResponseDto responseDto = PairBoardSaveResponseDto.builder()
            .id(pairBoard.getId()).boardLanguageId(boardLanguage.getId())
            .title(pairBoard.getTitle()).content(pairBoard.getContent()).ide(pairBoard.getIde())
            .proceed(pairBoard.getProceed()).runningTime(pairBoard.getRunningTime())
            .category(pairBoard.getCategory()).runningDate(pairBoard.getRunningDate())
            .createdAt(pairBoard.getCreatedAt()).updatedAt(pairBoard.getUpdatedAt())
            .status(pairBoard.getStatus()).viewCount(pairBoard.getViewCount())
            .language(languageList).build();

        return BaseResponseDto.success(responseDto);
    }


    // 페어 목록 글 전체 보기
    @Transactional(readOnly = true)
    public BaseResponseDto<?> getAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size); // 페이징 처리

        List<PairBoard> pairBoards = pairBoardRepository.findAllBySearch(pageable, keyword);
        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();
        for (PairBoard pairBoard : pairBoards) {
            // BoardLanguage정보를 Response에 담아주기 위한 객체 생성
            Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findById(
                pairBoard.getBoardLanguage().getId());
            List<String> languageList = new ArrayList<>();
            languageCheck(boardLanguage, languageList);
            boardAllResponseDtos.add(
                BoardAllResponseDto.builder().id(pairBoard.getId()).title(pairBoard.getTitle())
                    .content(pairBoard.getContent()).ide(pairBoard.getIde())
                    .runningTime(pairBoard.getRunningTime()).proceed(pairBoard.getProceed())
                    .category(pairBoard.getCategory()).language(languageList)
                    .runningDate(pairBoard.getRunningDate()).status(pairBoard.getStatus())
                    .viewCount(pairBoard.getViewCount()).createdAt(pairBoard.getCreatedAt())
                    .updatedAt(pairBoard.getUpdatedAt()).build());

        }

        return BaseResponseDto.success(boardAllResponseDtos);
    }

    @Transactional(readOnly = true)
    public BaseResponseDto<?> detail(Long id) {
        PairBoard pairBoard = pairBoardRepository.findById(id)
            .orElseThrow(() -> new NullPointerException("NOT_FOUND_BOARD"));
        Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findById(id);
        List<String> languageList = new ArrayList<>();
        languageCheck(boardLanguage, languageList);

        return BaseResponseDto.success(
            BoardAllResponseDto.builder().id(pairBoard.getId()).title(pairBoard.getTitle())
                .content(pairBoard.getContent()).ide(pairBoard.getIde())
                .runningTime(pairBoard.getRunningTime()).proceed(pairBoard.getProceed())
                .category(pairBoard.getCategory()).language(languageList)
                .runningDate(pairBoard.getRunningDate()).status(pairBoard.getStatus())
                .viewCount(pairBoard.getViewCount()).createdAt(pairBoard.getCreatedAt())
                .updatedAt(pairBoard.getUpdatedAt()).build());
    }

    @Transactional
    public BaseResponseDto<?> update(PairBoardSaveRequestDto requestDto, Long id) {

        PairBoard pairBoard = pairBoardRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);

        BoardLanguage boardLanguage = boardLanguageRepository.findById(
            pairBoard.getBoardLanguage().getId()).orElseThrow(IllegalArgumentException::new);

        List<String> languageList = requestDto.getLanguage();
        BoardLanguageResponseDto boardLanguageResponseDto = getBoardLanguageResponseDto(
            languageList);

        boardLanguage.update(boardLanguageResponseDto.getCLanguage(),
            boardLanguageResponseDto.getCSharp(), boardLanguageResponseDto.getCPlusPlus(),
            boardLanguageResponseDto.getJavaScript(), boardLanguageResponseDto.getJava(),
            boardLanguageResponseDto.getPython(), boardLanguageResponseDto.getNodeJs(),
            boardLanguageResponseDto.getTypeScript());

        pairBoard.update(boardLanguage, requestDto.getTitle(), requestDto.getContent(),
            requestDto.getIde(), requestDto.getCategory(), requestDto.getRunningTime(),
            requestDto.getProceed(), requestDto.getRunningDate(), pairBoard.getStatus(),
            pairBoard.getViewCount());

        PairBoardSaveResponseDto responseDto = PairBoardSaveResponseDto.builder()
            .id(pairBoard.getId()).boardLanguageId(boardLanguage.getId())
            .title(pairBoard.getTitle()).content(pairBoard.getContent()).ide(pairBoard.getIde())
            .proceed(pairBoard.getProceed()).runningTime(pairBoard.getRunningTime())
            .category(pairBoard.getCategory()).runningDate(pairBoard.getRunningDate())
            .createdAt(pairBoard.getCreatedAt()).updatedAt(pairBoard.getUpdatedAt())
            .status(pairBoard.getStatus()).viewCount(pairBoard.getViewCount())
            .language(languageList).build();

        return BaseResponseDto.success(responseDto);

    }


    // 전체 보기 Response에 language를 Boolean 타입이 아닌 스트링 형태로 보내 위한 메서드
    public void languageCheck(Optional<BoardLanguage> boardLanguage, List<String> languageList) {
        if (boardLanguage.get().getCLanguage() == Boolean.TRUE) {
            languageList.add("cLanguage");
        }
        if (boardLanguage.get().getCSharp() == Boolean.TRUE) {
            languageList.add("cSharp");
        }
        if (boardLanguage.get().getCPlusPlus() == Boolean.TRUE) {
            languageList.add("cPlusPlus");
        }
        if (boardLanguage.get().getJavaScript() == Boolean.TRUE) {
            languageList.add("javaScript");
        }
        if (boardLanguage.get().getJava() == Boolean.TRUE) {
            languageList.add("java");
        }
        if (boardLanguage.get().getPython() == Boolean.TRUE) {
            languageList.add("python");
        }
        if (boardLanguage.get().getNodeJs() == Boolean.TRUE) {
            languageList.add("nodeJs");
        }
        if (boardLanguage.get().getTypeScript() == Boolean.TRUE) {
            languageList.add("typeScript");
        }
    }

    public BoardLanguageResponseDto getBoardLanguageResponseDto(List<String> languageList) {
        BoardLanguageResponseDto boardLanguageResponseDto = BoardLanguageResponseDto.builder()
            .cLanguage(false).cSharp(false).cPlusPlus(false).javaScript(false).java(false)
            .nodeJs(false).python(false).typeScript(false).build();
        for (String language : languageList) {
            if (language.equals("cLanguage")) {
                boardLanguageResponseDto.setCLanguage(true);
            }
            if (language.equals("cSharp")) {
                boardLanguageResponseDto.setCSharp(true);
            }
            if (language.equals("cPlusPlus")) {
                boardLanguageResponseDto.setCPlusPlus(true);
            }
            if (language.equals("javaScript")) {
                boardLanguageResponseDto.setJavaScript(true);
            }
            if (language.equals("java")) {
                boardLanguageResponseDto.setJava(true);
            }
            if (language.equals("python")) {
                boardLanguageResponseDto.setPython(true);
            }
            if (language.equals("nodeJs")) {
                boardLanguageResponseDto.setNodeJs(true);
            }
            if (language.equals("typeScript")) {
                boardLanguageResponseDto.setTypeScript(true);
            }
        }
        return boardLanguageResponseDto;
    }

}
