package com.example.task_management.repositories.board;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardRecord, BigInteger> {
    
}
