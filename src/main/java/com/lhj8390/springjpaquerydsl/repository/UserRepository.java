package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
