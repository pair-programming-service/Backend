package com.pair.website.repository;

import com.pair.website.domain.PairBoard;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PairBoardRepository extends JpaRepository<PairBoard, Long> {

    List<PairBoard> findAllByOrderByIdDesc(Pageable pageable);

}
