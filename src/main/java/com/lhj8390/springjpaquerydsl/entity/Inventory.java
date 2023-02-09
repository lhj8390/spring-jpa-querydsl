package com.lhj8390.springjpaquerydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "inventory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int amount;

    @Builder
    public Inventory(Long id, Item item, User user, int amount) {
        this.id = id;
        this.item = item;
        this.user = user;
        this.amount = amount;
    }

    public void changeAmount(int amount) {
        this.amount = amount;
    }

}
