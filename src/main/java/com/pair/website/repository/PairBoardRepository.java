package com.pair.website.repository;

import com.pair.website.domain.PairBoard;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PairBoardRepository extends JpaRepository<PairBoard, Long> {
    // title 및 content 키워드 검색
    @Query(value = "select P from PairBoard P where P.title like %:keyword% or P.content like %:keyword% order by P.id desc")
    List<PairBoard> findAllBySearch(Pageable pageable,@Param("keyword") String keyword);

}
