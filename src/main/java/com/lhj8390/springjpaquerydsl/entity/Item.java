package com.lhj8390.springjpaquerydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Builder
    public Item(Long id, String name, int price, ItemType type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
