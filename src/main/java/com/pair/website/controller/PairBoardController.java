package com.pair.website.controller;

import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.service.PairBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.pair.website.dto.BaseResponseDto;
import com.pair.website.service.PairBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequiredArgsConstructor
public class PairBoardController {


    private final PairBoardService pairBoardService;

    // 페어 게시물 작성 API
    @PostMapping("/api/board")
    public BaseResponseDto<?> save(
        @RequestBody PairBoardSaveRequestDto requestDto) {

        return pairBoardService.save(requestDto);
    }


    @GetMapping("/api/board/all")
    public BaseResponseDto<?> getAll(@RequestParam("page") int page,
        @RequestParam("size") int size,@RequestParam(value = "search",defaultValue = "")String keyword) {
        page = page - 1;
        return pairBoardService.getAll(page, size,keyword);
    }

}
