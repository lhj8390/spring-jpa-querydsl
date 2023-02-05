package com.lhj8390.springjpaquerydsl.dto.guild;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuildResponseDTO {

    private Long id;
    private String name;

    private int count;
}
