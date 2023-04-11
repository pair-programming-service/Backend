package com.pair.website.service;

import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.BaseResponseDto;
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

    // 페어 목록 글 전체 보기
    public BaseResponseDto<?> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size); // 페이징 처리

        List<PairBoard> pairBoards = pairBoardRepository.findAllByOrderByIdDesc(pageable); // id기준으로 내림차순 정렬

        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();
        for (PairBoard pairBoard : pairBoards) {
            // BoardLanguage정보를 Response에 담아주기 위한 객체 생성
            Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findById(pairBoard.getBoardLanguage().getId());

            boardAllResponseDtos.add(BoardAllResponseDto.builder()
                        .id(pairBoard.getId())
                        .title(pairBoard.getTitle())
                        .content(pairBoard.getContent())
                        .ide(pairBoard.getIde())
                        .runningTime(pairBoard.getRunningTime())
                        .proceed(pairBoard.getProceed())
                        .boardLanguage(BoardLanguageResponseDto.builder()
                                .C(boardLanguage.get().getC())
                                .cSharp(boardLanguage.get().getCSharp())
                                .cPlusPlus(boardLanguage.get().getCPlusPlus())
                                .javaScript(boardLanguage.get().getJavaScript())
                                .java(boardLanguage.get().getJava())
                                .python(boardLanguage.get().getPython())
                                .nodeJs(boardLanguage.get().getNodeJs())
                                .typeScript(boardLanguage.get().getTypeScript())
                                .build())
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
}
