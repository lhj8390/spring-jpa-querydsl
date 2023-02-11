package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.item.*;
import com.lhj8390.springjpaquerydsl.entity.Inventory;
import com.lhj8390.springjpaquerydsl.entity.Item;
import com.lhj8390.springjpaquerydsl.entity.ItemHistoryType;
import com.lhj8390.springjpaquerydsl.entity.User;
import com.lhj8390.springjpaquerydsl.repository.InventoryRepository;
import com.lhj8390.springjpaquerydsl.repository.ItemHistoryRepository;
import com.lhj8390.springjpaquerydsl.repository.ItemRepository;
import com.lhj8390.springjpaquerydsl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemHistoryRepository itemHistoryRepository;

    /**
     * 유저에 따른 inventory 데이터 찾기
     * @param userId 유저 식별자 값
     * @return List<InventoryResponseDTO>
     */
    public List<InventoryResponseDTO> getInventory(Long userId) {
        List<Inventory> inventoryList = inventoryRepository.findAllByUserId(userId);

        return inventoryList.stream().map(
                (inventory ->
                    InventoryResponseDTO.builder()
                            .inventory(inventory)
                            .build())
                ).collect(Collectors.toList());
    }

    /**
     * 인벤토리에 아이템 넣기 및 Item 이력 추가 (코인 제한)
     * @param dto InventorySaveRequestDTO
     * @exception IllegalArgumentException 해당 item 이 없습니다. 해당 user 가 없습니다.
     * @exception IllegalStateException 코인이 부족합니다.
     */
    public void insertItem(InventorySaveRequestDTO dto) {
        Item item = isValidItem(dto.getItemId());
        User user = isValidUser(dto.getUserId()).get(0);

        if (user.getCoin() < item.getPrice() * dto.getAmount()) {
            throw new IllegalStateException("코인이 부족합니다.");
        }

        InventorySaveDTO saveDTO = InventorySaveDTO.builder()
                                                .item(item)
                                                .user(user)
                                                .amount(dto.getAmount())
                                                .build();
        Inventory inventory = inventoryRepository.save(saveDTO.toEntity());

        addHistory(inventory, ItemHistoryType.INSERT);
    }

    /**
     * 인벤토리에 아이템 삭제 및 Item 이력 추가
     * @param dto InventoryRemoveRequestDTO
     * @exception IllegalArgumentException 해당 inventory 가 없습니다.
     * @exception IllegalStateException 수량이 부족합니다.
     */
    @Transactional
    public void removeItem(InventoryRemoveRequestDTO dto) {

        Inventory inventory = isValidInventory(dto.getInventoryId());
        int diff = inventory.getAmount() - dto.getAmount();

        if (isAmountPositive(diff)) {
            inventory.changeAmount(diff);
        } else {
            inventoryRepository.delete(inventory);
        }

        addHistory(inventory, ItemHistoryType.REMOVE);
    }

    /**
     * 인벤토리 아이템 교환 및 Item 이력 추가
     * @param dto InventoryExchangeRequestDTO
     * @exception IllegalArgumentException 해당 item 이 없습니다. 해당 user 가 없습니다.
     * @exception IllegalStateException 인벤토리가 없습니다. 수량이 부족합니다.
     */
    @Transactional
    public void exchangeItem(InventoryExchangeRequestDTO dto) {
        Item item = isValidItem(dto.getItemId());
        List<User> userList = isValidUser(dto.getFromUserId(), dto.getToUserId());

        // id 키값으로 User 를 찾는 map 생성
        Map<Long, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        User fromUser = userMap.get(dto.getFromUserId());
        User toUser = userMap.get(dto.getToUserId());

        Inventory fromInven = inventoryRepository.findByUserIdAndItemId(fromUser.getId(), item.getId())
                .orElseThrow(() -> new IllegalStateException("인벤토리가 없습니다."));

        Inventory toInven = inventoryRepository.findByUserIdAndItemId(toUser.getId(), item.getId())
                .orElse(null);

        int diff = fromInven.getAmount() - dto.getAmount();

        if (isAmountPositive(diff)) {
            fromInven.changeAmount(diff);
        } else {
            inventoryRepository.delete(fromInven);
        }

        if (toInven != null) {
            toInven.changeAmount(toInven.getAmount() + dto.getAmount());
        } else {
            InventorySaveDTO saveDTO = InventorySaveDTO.builder()
                                                        .item(item)
                                                        .user(toUser)
                                                        .amount(dto.getAmount())
                                                        .build();

            toInven = inventoryRepository.save(saveDTO.toEntity());
        }

        addHistory(fromInven, ItemHistoryType.EXCHANGE);
        addHistory(toInven, ItemHistoryType.EXCHANGE);
    }

    /**
     * 아이템 장착 - 타입 및 인벤토리 아이템 한정
     * @param dto EquipRequestDTO
     * @exception IllegalStateException 인벤토리에 있는 아이템이 아닙니다. 타입이 맞지 않아 장착할 수 없습니다.
     */
    @Transactional
    public void equip(EquipRequestDTO dto) {
        Inventory inventory = inventoryRepository.findByUserIdAndItemId(dto.getUserId(), dto.getItemId())
                .orElseThrow(() -> new IllegalStateException("인벤토리에 있는 아이템이 아닙니다."));
        User user = inventory.getUser();
        Item item = inventory.getItem();

        if (!item.getType().name().equals(user.getType().name())) {
            throw new IllegalStateException("타입이 맞지 않아 장착할 수 없습니다.");
        }
        user.equip(item.getId());
    }

    /**
     * item id 가 유효한지 체크
     * @param id item 식별자 값
     * @return Item
     * @exception IllegalArgumentException 해당 item 이 없습니다.
     */
    private Item isValidItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 item 이 없습니다."));
    }

    /**
     * user id 가 유효한지 체크
     * @param ids user 식별자 값들
     * @return List<User>
     * @exception IllegalArgumentException 해당 user 가 없습니다.
     */
    private List<User> isValidUser(Long ...ids) {
        List<User> userList = userRepository.findByIdIn(Arrays.asList(ids));
        if (userList.size() != ids.length) {
            throw new IllegalArgumentException("해당 user 가 없습니다.");
        }
        return userList;
    }

    /**
     * inventory id 가 유효한지 체크
     * @param id inventory 식별자 값
     * @return Inventory
     * @exception IllegalArgumentException 해당 inventory 가 없습니다.
     */
    private Inventory isValidInventory(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 inventory 가 없습니다."));
    }

    /**
     * item history 추가
     * @param inventory 변경이 일어난 inventory 객체
     * @param historyType history 유형
     */
    private void addHistory(Inventory inventory, ItemHistoryType historyType) {
        ItemHistorySaveRequestDTO historyDTO = ItemHistorySaveRequestDTO.builder()
                .inventory(inventory)
                .type(historyType)
                .build();

        itemHistoryRepository.save(historyDTO.toEntity());
    }

    /**
     * 두 객체의 amount 비교 값이 양수인지 체크
     * @param diff 두 객체의 amount 비교 값
     * @return boolean
     * @exception IllegalStateException 수량이 부족합니다.
     */
    private boolean isAmountPositive(int diff) {
        if (diff < 0) throw new IllegalStateException("수량이 부족합니다.");
        return diff > 0;
    }
}
