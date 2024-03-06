package com.board.www.app.board.repository;

import com.board.www.app.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardDslRepository { }
