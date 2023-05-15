package com.pair.website.repository;

import com.pair.website.domain.PairBoard;

import com.pair.website.repository.querydsl.PairBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PairBoardRepository extends JpaRepository<PairBoard, Long>, PairBoardRepositoryCustom {
    Optional<PairBoard> findById(Long id);

    @Query("select p from PairBoard p where p.member.id = :memberId")
    List<PairBoard> findAllByMemberId(@Param("memberId") Long memberId);

}
