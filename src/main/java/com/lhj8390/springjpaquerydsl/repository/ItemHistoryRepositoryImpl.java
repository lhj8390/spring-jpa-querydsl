package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.dto.item.ItemHistoryResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.item.ItemHistorySearchDTO;
import com.lhj8390.springjpaquerydsl.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


import java.util.List;

import static com.lhj8390.springjpaquerydsl.entity.QItem.*;
import static com.lhj8390.springjpaquerydsl.entity.QItemHistory.*;
import static com.lhj8390.springjpaquerydsl.entity.QUser.*;

@Repository
@RequiredArgsConstructor
public class ItemHistoryRepositoryImpl implements ItemHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ItemHistoryResponseDTO> getAllHistory(ItemHistorySearchDTO dto, Pageable pageable) {
        List<ItemHistoryResponseDTO> content = queryFactory.select(Projections.fields(
                ItemHistoryResponseDTO.class,
                itemHistory.user.username,
                itemHistory.item.name.as("itemName"),
                itemHistory.item.type,
                itemHistory.createdDate,
                new CaseBuilder()
                        .when(itemHistory.type.eq(ItemHistoryType.EXCHANGE))
                        .then("교환")
                        .when(itemHistory.type.eq(ItemHistoryType.INSERT))
                        .then("구입")
                        .when(itemHistory.type.eq(ItemHistoryType.REMOVE))
                        .then("삭제")
                        .otherwise("-").as("detail")
        ))
                .from(itemHistory)
                .leftJoin(itemHistory.item, item)
                .leftJoin(itemHistory.user, user)
                .where(
                        itemNameLike(dto.getItemName()),
                        userNameLike(dto.getUserName()),
                        typeEq(dto.getItemType()),
                        historyTypeEq(dto.getHistoryType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(itemHistory.count())
                .from(itemHistory)
                .where(
                        itemNameLike(dto.getItemName()),
                        userNameLike(dto.getUserName()),
                        typeEq(dto.getItemType()),
                        historyTypeEq(dto.getHistoryType())
                );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression itemNameLike(String name) {
        return StringUtils.hasText(name) ? itemHistory.item.name.contains(name): null;
    }

    private BooleanExpression userNameLike(String name) {
        return StringUtils.hasText(name) ? itemHistory.user.username.contains(name): null;
    }

    private BooleanExpression historyTypeEq(ItemHistoryType type) {
        return type != null ? itemHistory.type.eq(type): null;
    }

    private BooleanExpression typeEq(ItemType type) {
        return type != null ? itemHistory.item.type.eq(type): null;
    }
}
