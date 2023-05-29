package com.pair.website.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private String githubLink;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YY-MM-dd HH:MM", timezone = "Asia/Seoul", locale = "en")
    private LocalDateTime createdAt;
    // @JsonIgnore
    private List<BoardListResponseDto> boardList;

    @Override
    public String toString() {
        return "MemberResponseDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", githubLink='" + githubLink + '\'' +
                ", createdAt=" + createdAt +
                ", pairBoards=" + boardList +
                '}';
    }
}
