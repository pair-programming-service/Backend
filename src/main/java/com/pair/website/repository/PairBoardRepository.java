package com.pair.website.repository;

import com.pair.website.domain.PairBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairBoardRepository extends JpaRepository<PairBoard, Long> {

}
