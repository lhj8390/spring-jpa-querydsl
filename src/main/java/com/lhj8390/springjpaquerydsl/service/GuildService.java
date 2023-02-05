package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.guild.GuildMemberResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.guild.GuildSaveRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class GuildService {

    public void create(GuildSaveRequestDTO requestDTO) {
        // TODO: 길드 생성 및 Guild 이력 추가
    }

    public void delete() {
        // TODO: 길드 삭제 및 Guild 이력 추가
    }

    public void join() {
        // TODO: 길드 가입 및 Guild 이력 추가
    }

    public void leave() {
        // TODO: 길드 나가기 및 Guild 이력 추가
    }

    public GuildMemberResponseDTO getMember() {
        // TODO: 길드 멤버 목록 노출 with Paging
        return GuildMemberResponseDTO.builder().build();
    }
}
