package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.dto.response.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.service.PairBoardService;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
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

    // 페어 게시물 작성 API
    @PostMapping("/api/board")
    public BaseResponseDto<?> save(
            @RequestBody PairBoardSaveRequestDto requestDto, HttpServletRequest request) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return pairBoardService.save(member,requestDto);
    }

    @GetMapping("/api/board/all")
    public BaseResponseDto<?> getAll(
                                     @RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "size",defaultValue = "20") int size,
                                     @RequestParam(value = "search", defaultValue = "") String keyword,@RequestParam(value = "category" , defaultValue = "") String category,
                                     @RequestParam(value = "status", defaultValue = "true") Boolean status,@RequestParam(value = "cLanguage", defaultValue = "false") Boolean cLanguage,
                                     @RequestParam(value = "cSharp", defaultValue = "false") Boolean cSharp, @RequestParam(value = "cPlusPlus", defaultValue = "false") Boolean cPlusPlus,
                                     @RequestParam(value = "javaScript", defaultValue = "false") Boolean javaScript , @RequestParam(value = "java", defaultValue = "false") Boolean java,
                                     @RequestParam(value = "python", defaultValue = "false") Boolean python, @RequestParam(value = "nodeJs", defaultValue = "false") Boolean nodeJs,
                                     @RequestParam(value = "typeScript", defaultValue = "false") Boolean typeScript) {
        page = page - 1;
        return pairBoardService.getAll(page,size, keyword,category,status,cLanguage,cSharp,cPlusPlus,javaScript,java,python,nodeJs,typeScript);
    }

    // 페어 게시물 상세 보기
    @GetMapping("/api/board/detail/{id}")
    public BaseResponseDto<?> detail(@PathVariable Long id) {
        return pairBoardService.detail(id);
    }

    // 페어 게시물 수정 API
    @PutMapping("/api/board/{id}")
    public BaseResponseDto<?> update(HttpServletRequest request, @RequestBody PairBoardSaveRequestDto requestDto,
                                    @PathVariable Long id) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return pairBoardService.update(member,requestDto, id);
    }

    // 게시물 삭제
    @DeleteMapping("/api/board/{id}")
    public BaseResponseDto<?> delete(@PathVariable Long id){
        return pairBoardService.deleteBoard(id);
    }
}
