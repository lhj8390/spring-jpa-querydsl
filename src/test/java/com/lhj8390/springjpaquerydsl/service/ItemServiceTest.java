package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.item.*;
import com.lhj8390.springjpaquerydsl.entity.*;
import com.lhj8390.springjpaquerydsl.repository.InventoryRepository;
import com.lhj8390.springjpaquerydsl.repository.ItemHistoryRepository;
import com.lhj8390.springjpaquerydsl.repository.ItemRepository;
import com.lhj8390.springjpaquerydsl.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemHistoryRepository itemHistoryRepository;

    @Autowired
    ItemService itemService;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        List<Inventory> inventoryList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();

        IntStream.range(0, 200)
                .forEach(i -> {
                    User user = User.builder()
                            .username("test" + i)
                            .type(UserType.WARRIOR)
                            .coin(100).build();

                    Item item = Item.builder()
                            .name("item"+ i)
                            .price(100 * (i + 1))
                            .type(ItemType.WARRIOR)
                            .build();

                    Inventory inventory = Inventory.builder()
                            .user(user)
                            .item(item)
                            .amount(i + 1)
                            .build();

                    userList.add(user);
                    itemList.add(item);
                    inventoryList.add(inventory);
                });

        userRepository.saveAll(userList);
        itemRepository.saveAll(itemList);
        inventoryRepository.saveAll(inventoryList);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 인벤토리_테스트() {

        List<InventoryResponseDTO> dtoList = itemService.getInventory(1L);

        assertEquals(dtoList.size(), 1);
        assertEquals(dtoList.get(0).getAmount(), 1);
        assertEquals(dtoList.get(0).getItemType(), ItemType.WARRIOR);
        assertEquals(dtoList.get(0).getItemName(), "item0");
    }

    @Test
    void 인벤토리에_아이템넣기_테스트() {
        Item item = itemRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        InventorySaveRequestDTO dto = InventorySaveRequestDTO.builder()
                .amount(1)
                .itemId(item.getId())
                .userId(user.getId())
                .build();

        itemService.insertItem(dto);

        // 인벤토리 테스트
        Inventory result = inventoryRepository.findAll().get(200);
        assertEquals(result.getItem().getId(), item.getId());
        assertEquals(result.getUser().getId(), user.getId());
        assertEquals(result.getAmount(), 1);

        // 히스토리 테스트
        ItemHistory history = itemHistoryRepository.findAll().get(0);
        assertEquals(history.getItem(), item);
        assertEquals(history.getUser(), user);
        assertEquals(history.getType(), ItemHistoryType.INSERT);
    }

    @Test
    void 인벤토리_코인부족() {
        Item item = itemRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        InventorySaveRequestDTO dto = InventorySaveRequestDTO.builder()
                .amount(10000)
                .itemId(item.getId())
                .userId(user.getId())
                .build();

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                itemService.insertItem(dto)
        );

        assertEquals("코인이 부족합니다.", exception.getMessage());
    }

    @Test
    void 아이템_또는_유저_없음() {
        Item item = itemRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        InventorySaveRequestDTO noItemDto = InventorySaveRequestDTO.builder()
                .amount(1)
                .itemId(0L)
                .userId(user.getId())
                .build();

        InventorySaveRequestDTO noUserDto = InventorySaveRequestDTO.builder()
                .amount(1)
                .itemId(item.getId())
                .userId(0L)
                .build();

        Throwable noItemException = assertThrows(IllegalArgumentException.class, () ->
                itemService.insertItem(noItemDto)
        );
        Throwable noUserException = assertThrows(IllegalArgumentException.class, () ->
                itemService.insertItem(noUserDto)
        );

        assertEquals("해당 item 이 없습니다.", noItemException.getMessage());
        assertEquals("해당 user 가 없습니다.", noUserException.getMessage());
    }

    @Test
    void 아이템_삭제_테스트() {
        Inventory inventory = inventoryRepository.findAll().get(0);
        Inventory inventory2 = inventoryRepository.findAll().get(1);

        InventoryRemoveRequestDTO deleted = InventoryRemoveRequestDTO.builder()
                .inventoryId(inventory.getId())
                .amount(1)
                .build();

        InventoryRemoveRequestDTO notDeleted = InventoryRemoveRequestDTO.builder()
                .inventoryId(inventory2.getId())
                .amount(1)
                .build();

        itemService.removeItem(notDeleted);
        itemService.removeItem(deleted);

        // 인벤토리 테스트
        Inventory deletedResult = inventoryRepository.findById(inventory.getId()).orElse(null);
        assertNull(deletedResult);

        Inventory notDeletedResult = inventoryRepository.findById(inventory2.getId()).orElse(null);
        assert notDeletedResult != null;
        assertEquals(notDeletedResult.getAmount(), 1);
        assertEquals(notDeletedResult.getItem(), inventory2.getItem());

        // 히스토리 테스트
        ItemHistory history = itemHistoryRepository.findAll().get(0);
        assertEquals(history.getType(), ItemHistoryType.REMOVE);
        assertEquals(history.getAmount(), 1);
        assertEquals(history.getItem().getId(), inventory2.getItem().getId());
    }

    @Test
    void 아이템_삭제_수량부족() {
        Inventory inventory = inventoryRepository.findAll().get(0);

        InventoryRemoveRequestDTO dto = InventoryRemoveRequestDTO.builder()
                .inventoryId(inventory.getId())
                .amount(100)
                .build();

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                itemService.removeItem(dto)
        );

        assertEquals("수량이 부족합니다.", exception.getMessage());
    }

    @Test
    void 아이템_교환_테스트() {
        Inventory fromInventory = inventoryRepository.findAll().get(1); // 기존 수량 2개
        Inventory toInventory = inventoryRepository.findAll().get(0);  // 기존 수량 1개

        InventoryExchangeRequestDTO dto = InventoryExchangeRequestDTO.builder()
                .itemId(fromInventory.getItem().getId())
                .fromUserId(fromInventory.getUser().getId())
                .toUserId(toInventory.getUser().getId())
                .amount(1)
                .build();

        itemService.exchangeItem(dto);

        Inventory exchangedFrom = inventoryRepository.findById(fromInventory.getId()).orElse(null);
        Inventory exchangedTo = inventoryRepository.findById(toInventory.getId()).orElse(null);

        assert exchangedFrom != null;
        assert exchangedTo != null;

        assertEquals(exchangedFrom.getAmount(), 1);
        assertEquals(exchangedTo.getAmount(), 1);
    }

    @Test
    void 장착_테스트() {
        Inventory inventory = inventoryRepository.findAll().get(0);

        EquipRequestDTO dto = EquipRequestDTO.builder()
                .itemId(inventory.getItem().getId())
                .userId(inventory.getUser().getId())
                .build();

        itemService.equip(dto);

        assertEquals(inventory.getUser().getEquipment(), dto.getItemId());
    }

    @Test
    void 장착_타입_제한() {
        User user = User.builder()
                .type(UserType.WARRIOR)
                .build();

        Item item = Item.builder()
                .type(ItemType.MAGE)
                .build();

        Inventory inventory = Inventory.builder()
                .user(user)
                .item(item)
                .build();

        userRepository.save(user);
        itemRepository.save(item);
        inventoryRepository.save(inventory);
        EquipRequestDTO dto = EquipRequestDTO.builder()
                .itemId(item.getId())
                .userId(user.getId())
                .build();

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                itemService.equip(dto)
        );

        assertEquals("타입이 맞지 않아 장착할 수 없습니다.", exception.getMessage());
    }

    @Test
    void 장착_인벤토리에_없는_아이템() {
        EquipRequestDTO dto = EquipRequestDTO.builder()
                .itemId(0L)
                .userId(0L)
                .build();

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                itemService.equip(dto)
        );

        assertEquals("인벤토리에 있는 아이템이 아닙니다.", exception.getMessage());
    }

}