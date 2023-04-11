package com.pair.website.controller;

import com.pair.website.dto.BaseResponseDto;
import com.pair.website.service.PairBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PairBoardController {
    private final PairBoardService pairBoardService;

    @RequestMapping(path = "/api/board/all",method = RequestMethod.GET)
    public BaseResponseDto<?> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        page = page -1;
        return pairBoardService.getAll(page,size);
    }
}
