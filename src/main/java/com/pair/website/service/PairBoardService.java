package com.pair.website.service;

import com.pair.website.domain.PairBoard;
import com.pair.website.dto.PairBoardSaveRequestDto;
import com.pair.website.dto.PairBoardSaveResponseDto;
import com.pair.website.repository.PairBoardRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PairBoardService {


    private final PairBoardRepository pairBoardRepository;

    public PairBoardSaveResponseDto save(PairBoardSaveRequestDto requestDto) {

        PairBoard pairBoard = requestDto.toEntity();
        pairBoardRepository.save(pairBoard);

        return new PairBoardSaveResponseDto(pairBoard);
    }

}
