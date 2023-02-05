package com.lhj8390.springjpaquerydsl.dto.guild;

import com.lhj8390.springjpaquerydsl.dto.user.UserResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuildMemberResponseDTO {
    private String name;
    private List<UserResponseDTO> memberList = new ArrayList<>();

    @Builder
    public GuildMemberResponseDTO(String name, List<UserResponseDTO> memberList) {
        this.name = name;
        this.memberList = memberList;
    }
}
