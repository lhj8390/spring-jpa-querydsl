package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.entity.Item;
import com.lhj8390.springjpaquerydsl.entity.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void item_생성() {

        LocalDateTime now = LocalDateTime.now();

        Item item = Item.builder()
                .id(1L)
                .name("test")
                .price(100)
                .type(ItemType.WARRIOR)
                .build();

        Item result = itemRepository.save(item);

        assertThat(result.getCreatedDate()).isAfter(now);
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getType()).isEqualTo(ItemType.WARRIOR);
    }
}