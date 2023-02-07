package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.user.UserResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.user.UserSearchRequestDTO;
import com.lhj8390.springjpaquerydsl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 멤버 검색 (이름, 타입)
     * @Param dto UserSearchRequestDTO
     * @Param pageable
     * @return Page<UserResponseDTO>
     */
    public Page<UserResponseDTO> getUserSearch(UserSearchRequestDTO dto, Pageable pageable) {
        return userRepository.getUsers(dto, pageable);
    }
}
