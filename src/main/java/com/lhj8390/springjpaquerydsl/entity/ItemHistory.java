package com.lhj8390.springjpaquerydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemHistory extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int amount;

    @Enumerated(EnumType.STRING)
    private ItemHistoryType type;

    @Builder
    public ItemHistory(Long id, User user, Item item, int amount, ItemHistoryType type) {
        this.id = id;
        this.user = user;
        this.item = item;
        this.amount = amount;
        this.type = type;
    }
}
