package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.response.PageResponseDto;
import com.pair.website.service.PairBoardService;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PairBoardController {


    private final PairBoardService pairBoardService;
    private final PublicMethod publicMethod;

    /**
     * 페어 게시물 작성 API
     *
     * @param requestDto 게시물 정보
     * @return BaseResponseDto<MemberResponseDto> 200 OK, 작성된 게시물 정보
     */
    @PostMapping("/api/board")
    public BaseResponseDto<?> save(
            @RequestBody PairBoardSaveRequestDto requestDto, HttpServletRequest request) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return pairBoardService.save(member, requestDto);
    }

    /**
     * 페어 게시물 전체 조회 API
     *
     * @param page    페이지
     * @param size    보여줄 게시물 수
     * @param keyword 검색어
     * @return BaseResponseDto<List < BoardAllResponseDto>> 200 OK, 게시물 전체 정보
     */
    @GetMapping("/api/board/all")
    public PageResponseDto<?> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", defaultValue = "") String keyword, @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "status", defaultValue = "true") Boolean status, @RequestParam(value = "cLanguage", defaultValue = "false") Boolean cLanguage,
            @RequestParam(value = "cSharp", defaultValue = "false") Boolean cSharp, @RequestParam(value = "cPlusPlus", defaultValue = "false") Boolean cPlusPlus,
            @RequestParam(value = "javaScript", defaultValue = "false") Boolean javaScript, @RequestParam(value = "java", defaultValue = "false") Boolean java,
            @RequestParam(value = "python", defaultValue = "false") Boolean python, @RequestParam(value = "nodeJs", defaultValue = "false") Boolean nodeJs,
            @RequestParam(value = "typeScript", defaultValue = "false") Boolean typeScript) {
        page = page - 1;
        return pairBoardService.getAll(page, size, keyword, category, status, cLanguage, cSharp, cPlusPlus, javaScript, java, python, nodeJs, typeScript);
    }

    /**
     * 페어 게시물 상세 조회 API
     *
     * @param id 게시물 id(PK)
     * @return BaseResponseDto<BoardAllResponseDto> 200 OK, 게시물 상세 정보
     */
    @GetMapping("/api/board/detail/{id}")
    public BaseResponseDto<?> detail(@PathVariable Long id) {
        return pairBoardService.detail(id);
    }

    /**
     * 페어 게시물 수정 API
     *
     * @param requestDto 수정될 게시물 정보
     * @return BaseResponseDto<BoardAllResponseDto> 200 OK, 수정된 게시물 상세 정보
     */
    @PutMapping("/api/board/{id}")
    public BaseResponseDto<?> update(HttpServletRequest request, @RequestBody PairBoardSaveRequestDto requestDto,
                                     @PathVariable Long id) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return pairBoardService.update(member, requestDto, id);
    }

    /**
     * 페어 게시물 삭제 API
     *
     * @param id 게시물 id(PK)
     * @return BaseResponseDto<?> 200 OK
     */
    @DeleteMapping("/api/board/{id}")
    public BaseResponseDto<?> delete(@PathVariable Long id) {
        return pairBoardService.deleteBoard(id);
    }
}
