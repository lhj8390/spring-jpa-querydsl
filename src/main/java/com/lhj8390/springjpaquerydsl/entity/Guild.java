package com.lhj8390.springjpaquerydsl.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guild extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "guild_id")
    private Long id;

    private String name;
}
