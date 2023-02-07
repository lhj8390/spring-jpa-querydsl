package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.dto.user.UserResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.user.UserSearchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserResponseDTO> getUsers(UserSearchRequestDTO dto, Pageable pageable);
}
