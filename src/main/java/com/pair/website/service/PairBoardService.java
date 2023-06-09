package com.pair.website.service;

import com.pair.website.domain.BoardLanguage;
import com.pair.website.domain.Member;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.response.PairBoardSaveResponseDto;
import com.pair.website.dto.response.BoardAllResponseDto;
import com.pair.website.dto.response.BoardLanguageResponseDto;
import com.pair.website.dto.response.PageResponseDto;
import com.pair.website.jwt.TokenProvider;
import com.pair.website.repository.BoardLanguageRepository;
import com.pair.website.repository.PairBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    public BaseResponseDto<?> save(Member member,PairBoardSaveRequestDto requestDto) {
        PairBoard pairBoard = requestDto.toEntity(member);
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

        boardLanguageRepository.save(boardLanguage);

        PairBoardSaveResponseDto responseDto = PairBoardSaveResponseDto.builder()

            .id(pairBoard.getId()).boardLanguageId(boardLanguage.getId()).member(member.getNickname())
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
    public PageResponseDto<?> getAll(int page, int size, String keyword,String category, Boolean status,Boolean cLanguage,
                                     Boolean cSharp , Boolean cPlusPlus, Boolean javaScript,
                                     Boolean java, Boolean python, Boolean nodeJs, Boolean typeScript) {
        Pageable pageable = PageRequest.of(page, size); // 페이징 처리
        
        Page<PairBoard> pairBoards = pairBoardRepository.findDynamicQuery(pageable, keyword, category,status,cLanguage, cSharp, cPlusPlus,
                    javaScript, java, python, nodeJs, typeScript);


        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();
        for (PairBoard pairBoard : pairBoards) {
            boardList(pairBoard,boardAllResponseDtos);
        }
        return PageResponseDto.success(boardAllResponseDtos,pairBoards.getTotalPages());
    }

    @Transactional
    public BaseResponseDto<?> detail(Long id) {
        PairBoard pairBoard = pairBoardRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("NOT_FOUND_BOARD"));
        Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findByPairBoardId(id);
        List<String> languageList = new ArrayList<>();
        languageCheck(boardLanguage, languageList);

        pairBoard.update(pairBoard.getViewCount() + 1);

        return BaseResponseDto.success(
                BoardAllResponseDto.builder().id(pairBoard.getId()).nickname(pairBoard.getMember().getNickname()).title(pairBoard.getTitle())
                        .content(pairBoard.getContent()).ide(pairBoard.getIde())
                        .runningTime(pairBoard.getRunningTime()).proceed(pairBoard.getProceed())
                        .category(pairBoard.getCategory()).language(languageList)
                        .runningDate(pairBoard.getRunningDate()).status(pairBoard.getStatus())
                        .viewCount(pairBoard.getViewCount()).createdAt(pairBoard.getCreatedAt())
                        .updatedAt(pairBoard.getUpdatedAt()).build());
    }

    @Transactional
    public BaseResponseDto<?> update(Member member,PairBoardSaveRequestDto requestDto, Long id) {
        PairBoard pairBoard = pairBoardRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        BoardLanguage boardLanguage = boardLanguageRepository.findById(
                pairBoard.getBoardLanguage().getId()).orElseThrow(IllegalArgumentException::new);

        if(!member.getId().equals(pairBoard.getMember().getId()))
            return BaseResponseDto.fail("ONLY_THE_AUTHOR_CAN_EDIT","작성자만 수정 가능합니다.");

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
                requestDto.getProceed(), requestDto.getRunningDate(), requestDto.getStatus(),
                pairBoard.getViewCount());

        PairBoardSaveResponseDto responseDto = PairBoardSaveResponseDto.builder()
                .id(pairBoard.getId()).member(member.getNickname()).boardLanguageId(boardLanguage.getId())
                .title(pairBoard.getTitle()).content(pairBoard.getContent()).ide(pairBoard.getIde())
                .proceed(pairBoard.getProceed()).runningTime(pairBoard.getRunningTime())
                .category(pairBoard.getCategory()).runningDate(pairBoard.getRunningDate())
                .createdAt(pairBoard.getCreatedAt()).updatedAt(pairBoard.getUpdatedAt())
                .status(pairBoard.getStatus()).viewCount(pairBoard.getViewCount())
                .language(languageList).build();

        return BaseResponseDto.success(responseDto);

    }


    // 게시물 삭제
    public BaseResponseDto<?> deleteBoard(Long id) {
        Optional<PairBoard> pairBoard = pairBoardRepository.findById(id);
        if (!pairBoard.isPresent()) return BaseResponseDto.fail("NOT_FOUND_BOARD", "게시물을 찾을 수 없습니다.");
        BoardLanguage boardLanguage = boardLanguageRepository.findByPairBoardId(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );

        pairBoardRepository.deleteById(id);
        boardLanguageRepository.delete(boardLanguage);

        return BaseResponseDto.success("게시물이 삭제되었습니다.");
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
    public List<BoardAllResponseDto> boardList(PairBoard pairBoard,List<BoardAllResponseDto> boardAllResponseDtos){
        // BoardLanguage정보를 Response에 담아주기 위한 객체 생성
        Optional<BoardLanguage> boardLanguage = boardLanguageRepository.findById(
                pairBoard.getBoardLanguage().getId());
        List<String> languageList = new ArrayList<>();
        languageCheck(boardLanguage, languageList);
        boardAllResponseDtos.add(
                BoardAllResponseDto.builder().id(pairBoard.getId()).nickname(pairBoard.getMember().getNickname()).title(pairBoard.getTitle())
                        .content(pairBoard.getContent()).ide(pairBoard.getIde())
                        .runningTime(pairBoard.getRunningTime()).proceed(pairBoard.getProceed())
                        .category(pairBoard.getCategory()).language(languageList)
                        .runningDate(pairBoard.getRunningDate()).status(pairBoard.getStatus())
                        .viewCount(pairBoard.getViewCount()).createdAt(pairBoard.getCreatedAt())
                        .updatedAt(pairBoard.getUpdatedAt()).build());
        return boardAllResponseDtos;
    }
}
