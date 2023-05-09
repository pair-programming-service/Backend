package com.pair.website.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileImage;

    @Column
    private String githubLink;

    @OneToMany(mappedBy = "member")
    private List<PairBoard> pairBoards = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String nickname, String password, String profileImage,
                  String githubLink, List<PairBoard> pairBoards) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.githubLink = githubLink;
        this.pairBoards = pairBoards;
    }

    public Member update(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;

        return this;
    }

}
