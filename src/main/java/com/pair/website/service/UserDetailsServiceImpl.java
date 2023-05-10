package com.pair.website.service;

import com.pair.website.domain.Member;
import com.pair.website.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //username을 통해서 Member객체를 가져와서 UserDetailImpl안에 세팅한 후 반환

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 업습니다.")
        );
        //username이 있는지 없는지 확인후 UserDetailsImpl에 담아주고 객체 반환
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setMember(member);
        return userDetails;
    }
}
