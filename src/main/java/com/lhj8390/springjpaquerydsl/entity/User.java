package com.lhj8390.springjpaquerydsl.entity;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString
public class User extends BaseEntity implements Persistable<Long> {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private int coin;

    private Long equipment;

    @Builder
    public User(Long id, String username, UserType type, int coin, Long equipment) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.coin = coin;
        this.equipment = equipment;
    }

    @Override
    public boolean isNew() {
        // save method 수행 시 select query 로 새로운 데이터인지 확인
        // select query 차단 위해 Persistable interface 구현
        return getCreatedDate() == null;
    }

    public void equip(Long equipment) {
        this.equipment = equipment;
    }

}
