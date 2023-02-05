package com.lhj8390.springjpaquerydsl.dto.user;

import com.lhj8390.springjpaquerydsl.entity.UserType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDTO {

    private Long id;
    private String name;
    private UserType type;
    private int coin;

    @Builder
    public UserResponseDTO(Long id, String name, UserType type, int coin) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coin = coin;
    }
}
