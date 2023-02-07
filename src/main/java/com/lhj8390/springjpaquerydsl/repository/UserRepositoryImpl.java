package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.dto.user.UserResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.user.UserSearchRequestDTO;
import com.lhj8390.springjpaquerydsl.entity.User;
import com.lhj8390.springjpaquerydsl.entity.UserType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.lhj8390.springjpaquerydsl.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<UserResponseDTO> getUsers(UserSearchRequestDTO dto, Pageable pageable) {
        List<UserResponseDTO> content = queryFactory
                .select(Projections.fields(
                            UserResponseDTO.class,
                            user.id,
                            user.username.as("name"),
                            user.type,
                            user.coin))
                .from(user)
                .where(
                        nameLike(dto.getName()),
                        typeEq(dto.getType())
                        )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<User> countQuery = queryFactory.select(user)
                                                .from(user)
                                                .where(
                                                        nameLike(dto.getName()),
                                                        typeEq(dto.getType())
                                                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression nameLike(String name) {
        return name != null ? user.username.contains(name): null;
    }

    private BooleanExpression typeEq(UserType type) {
        return type != null ? user.type.eq(type): null;
    }
}