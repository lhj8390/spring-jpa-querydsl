package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.user.UserResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.user.UserSearchRequestDTO;
import com.lhj8390.springjpaquerydsl.entity.User;
import com.lhj8390.springjpaquerydsl.entity.UserType;
import com.lhj8390.springjpaquerydsl.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @Transactional
    void 유저_검색_테스트() {
        UserType[] userTypes = { UserType.WARRIOR, UserType.MAGE, UserType.ROGUE };
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .username("test" + i)
                    .coin(i * 100)
                    .type(userTypes[i % 3])
                    .build();
            userList.add(user);
        }
        userRepository.saveAll(userList);
        PageRequest pageRequest = PageRequest.of(0, 5);

        UserSearchRequestDTO searchDTO1 = UserSearchRequestDTO.builder()
                .type(UserType.WARRIOR)
                .build();

        Page<UserResponseDTO> result1 = userService.getUserSearch(searchDTO1, pageRequest);

        assertEquals(result1.getContent().size(), 5);
        assertEquals(result1.getContent().get(0).getType(), UserType.WARRIOR);

        UserSearchRequestDTO searchDTO2 = UserSearchRequestDTO.builder()
                .name("99")
                .build();

        Page<UserResponseDTO> result2 = userService.getUserSearch(searchDTO2, pageRequest);

        assertEquals(result2.getContent().size(), 1);
        assertEquals(result2.getContent().get(0).getType(), UserType.WARRIOR);

    }
}