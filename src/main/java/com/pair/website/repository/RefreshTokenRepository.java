package com.pair.website.repository;

import com.pair.website.domain.Member;
import com.pair.website.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByMember(Member member);
}
