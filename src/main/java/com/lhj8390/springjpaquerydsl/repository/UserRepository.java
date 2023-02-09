package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    List<User> findByIdIn(List<Long> ids);
}
