package com.pair.website.repository;

import com.pair.website.domain.PairBoard;

import com.pair.website.repository.querydsl.PairBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PairBoardRepository extends JpaRepository<PairBoard, Long>, PairBoardRepositoryCustom {
    Optional<PairBoard> findById(Long id);

}
