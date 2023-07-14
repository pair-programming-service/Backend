package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.response.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.response.PairBoardSaveResponseDto;
import com.pair.website.service.PairBoardService;
import com.pair.website.util.PublicMethod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "PairBoard", description = "게시물 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PairBoardController {


    private final PairBoardService pairBoardService;
    private final PublicMethod publicMethod;

    // 페어 게시물 작성 API
    @Operation(summary = "save board", description = "페어 모집 글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PostMapping("/board")
    public BaseResponseDto<?> save(
            @RequestBody PairBoardSaveRequestDto requestDto, HttpServletRequest request) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return pairBoardService.save(member,requestDto);
    }
    @GetMapping("/board/all")
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
    @GetMapping("/board/detail/{id}")
    public BaseResponseDto<?> detail(@PathVariable Long id) {
        return pairBoardService.detail(id);
    }

    // 페어 게시물 수정 API
    @PutMapping("/board/{id}")
    public BaseResponseDto<?> update(HttpServletRequest request, @RequestBody PairBoardSaveRequestDto requestDto,
                                    @PathVariable Long id) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return pairBoardService.update(member,requestDto, id);
    }

    // 게시물 삭제
    @DeleteMapping("/board/{id}")
    public BaseResponseDto<?> delete(@PathVariable Long id){
        return pairBoardService.deleteBoard(id);
    }
}
