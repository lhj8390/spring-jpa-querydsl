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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    HistoryService historyService;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        List<Inventory> inventoryList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        List<ItemHistory> historyList = new ArrayList<>();

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

                    ItemHistory history = ItemHistory.builder()
                            .item(item)
                            .user(user)
                            .amount(5)
                            .type(ItemHistoryType.values()[i % 3])
                            .build();

                    userList.add(user);
                    itemList.add(item);
                    inventoryList.add(inventory);
                    historyList.add(history);
                });

        userRepository.saveAll(userList);
        itemRepository.saveAll(itemList);
        inventoryRepository.saveAll(inventoryList);
        itemHistoryRepository.saveAll(historyList);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void ????????????_?????????() {

        User user = userRepository.findAll().get(0);
        List<InventoryResponseDTO> dtoList = itemService.getInventory(user.getId());

        assertEquals(dtoList.size(), 1);
        assertEquals(dtoList.get(0).getAmount(), 1);
        assertEquals(dtoList.get(0).getItemType(), ItemType.WARRIOR);
        assertEquals(dtoList.get(0).getItemName(), "item0");
    }

    @Test
    void ???????????????_???????????????_?????????() {
        Item item = itemRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        InventorySaveRequestDTO dto = InventorySaveRequestDTO.builder()
                .amount(1)
                .itemId(item.getId())
                .userId(user.getId())
                .build();

        itemService.insertItem(dto);

        // ???????????? ?????????
        Inventory result = inventoryRepository.findAll().get(200);
        assertEquals(result.getItem().getId(), item.getId());
        assertEquals(result.getUser().getId(), user.getId());
        assertEquals(result.getAmount(), 1);

        // ???????????? ?????????
        ItemHistory history = itemHistoryRepository.findAll().get(0);
        assertEquals(history.getItem(), item);
        assertEquals(history.getUser(), user);
        assertEquals(history.getType(), ItemHistoryType.INSERT);
    }

    @Test
    void ????????????_????????????() {
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

        assertEquals("????????? ???????????????.", exception.getMessage());
    }

    @Test
    void ?????????_??????_??????_??????() {
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

        assertEquals("?????? item ??? ????????????.", noItemException.getMessage());
        assertEquals("?????? user ??? ????????????.", noUserException.getMessage());
    }

    @Test
    void ?????????_??????_?????????() {
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

        itemService.removeItem(deleted);
        itemService.removeItem(notDeleted);

        // ???????????? ?????????
        Inventory deletedResult = inventoryRepository.findById(inventory.getId()).orElse(null);
        assertNull(deletedResult);

        Inventory notDeletedResult = inventoryRepository.findById(inventory2.getId()).orElse(null);
        assert notDeletedResult != null;
        assertEquals(notDeletedResult.getAmount(), 1);
        assertEquals(notDeletedResult.getItem(), inventory2.getItem());

        // ???????????? ?????????
        ItemHistory history = itemHistoryRepository.findAll().get(201);
        assertEquals(history.getType(), ItemHistoryType.REMOVE);
        assertEquals(history.getAmount(), 1);
        assertEquals(history.getItem().getId(), inventory2.getItem().getId());
    }

    @Test
    void ?????????_??????_????????????() {
        Inventory inventory = inventoryRepository.findAll().get(0);

        InventoryRemoveRequestDTO dto = InventoryRemoveRequestDTO.builder()
                .inventoryId(inventory.getId())
                .amount(100)
                .build();

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                itemService.removeItem(dto)
        );

        assertEquals("????????? ???????????????.", exception.getMessage());
    }

    @Test
    void ?????????_??????_?????????() {
        Inventory fromInventory = inventoryRepository.findAll().get(1); // ?????? ?????? 2???
        Inventory toInventory = inventoryRepository.findAll().get(0);  // ?????? ?????? 1???

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
    void ??????_?????????() {
        Inventory inventory = inventoryRepository.findAll().get(0);

        EquipRequestDTO dto = EquipRequestDTO.builder()
                .itemId(inventory.getItem().getId())
                .userId(inventory.getUser().getId())
                .build();

        itemService.equip(dto);

        assertEquals(inventory.getUser().getEquipment(), dto.getItemId());
    }

    @Test
    void ??????_??????_??????() {
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

        assertEquals("????????? ?????? ?????? ????????? ??? ????????????.", exception.getMessage());
    }

    @Test
    void ??????_???????????????_??????_?????????() {
        EquipRequestDTO dto = EquipRequestDTO.builder()
                .itemId(0L)
                .userId(0L)
                .build();

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                itemService.equip(dto)
        );

        assertEquals("??????????????? ?????? ???????????? ????????????.", exception.getMessage());
    }

    @Test
    void ?????????_????????????_??????_??????() {
        ItemHistorySearchDTO dto = ItemHistorySearchDTO.builder()
                .build();
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<ItemHistoryResponseDTO> list = historyService.getAllItemHistory(dto, pageRequest);

        assertEquals(list.getContent().size(), 5);
    }

    @Test
    void ?????????_????????????_??????_??????() {
        ItemHistorySearchDTO findItem = ItemHistorySearchDTO.builder()
                .itemName("item0")
                .build();
        ItemHistorySearchDTO findUser = ItemHistorySearchDTO.builder()
                .userName("test0")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<ItemHistoryResponseDTO> findItemList = historyService.getAllItemHistory(findItem, pageRequest);
        Page<ItemHistoryResponseDTO> findUserList = historyService.getAllItemHistory(findUser, pageRequest);

        assertEquals(findItemList.getContent().size(), 1);
        assertEquals(findItemList.getContent().get(0).getItemName(), "item0");

        assertEquals(findUserList.getContent().size(), 1);
        assertEquals(findUserList.getContent().get(0).getUsername(), "test0");
    }

    @Test
    void ?????????_????????????_??????_??????() {
        ItemHistorySearchDTO findItemType = ItemHistorySearchDTO.builder()
                .itemType(ItemType.WARRIOR)
                .build();
        ItemHistorySearchDTO findHistoryType = ItemHistorySearchDTO.builder()
                .historyType(ItemHistoryType.INSERT)
                .build();

        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<ItemHistoryResponseDTO> findItemTypeList = historyService.getAllItemHistory(findItemType, pageRequest);
        Page<ItemHistoryResponseDTO> findHistoryTypeList = historyService.getAllItemHistory(findHistoryType, pageRequest);

        assertEquals(findItemTypeList.getContent().size(), 5);
        assertEquals(findItemTypeList.getTotalElements(), 200);
        assertEquals(findItemTypeList.getContent().get(0).getType(), ItemType.WARRIOR);

        assertEquals(findHistoryTypeList.getSize(), 5);
        assertEquals(findHistoryTypeList.getTotalElements(), 67);
        assertEquals(findHistoryTypeList.getContent().get(0).getDetail(), "??????");

    }

}