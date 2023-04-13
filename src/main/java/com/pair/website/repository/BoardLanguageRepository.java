package com.pair.website.repository;

import com.pair.website.domain.BoardLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLanguageRepository extends JpaRepository<BoardLanguage, Long> {

    Optional<BoardLanguage> findById(Long id);

    BoardLanguage findByPairBoardId(Long id);
}
