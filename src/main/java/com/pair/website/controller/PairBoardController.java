package com.pair.website.controller;


import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.service.PairBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
