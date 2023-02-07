package com.lhj8390.springjpaquerydsl.dto.user;

import com.lhj8390.springjpaquerydsl.entity.UserType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchRequestDTO {

    private String name;
    private UserType type;

    @Builder
    public UserSearchRequestDTO(String name, UserType type) {
        this.name = name;
        this.type = type;
    }
}
