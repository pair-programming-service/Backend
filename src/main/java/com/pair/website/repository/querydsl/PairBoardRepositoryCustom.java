package com.pair.website.repository.querydsl;

import com.pair.website.domain.PairBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PairBoardRepositoryCustom {
    // title 및 content 키워드 검색 , 유효 필터 검색
    Page<PairBoard> findDynamicQuery(Pageable pageable, @Param("search") String keyword,
                                             @Param("category") String category,
                                             @Param("cLanguage") Boolean cLanguage, @Param("cSharp") Boolean cSharp,
                                             @Param("cPlusPlus") Boolean cPlusPlus, @Param("javaScript") Boolean javaScript,
                                             @Param("java") Boolean java, @Param("python") Boolean python,
                                             @Param("nodeJs") Boolean nodeJs, @Param("typeScript") Boolean typeScript);
}
