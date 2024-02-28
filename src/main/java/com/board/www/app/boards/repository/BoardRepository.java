package com.board.www.app.boards.repository;

import com.board.www.app.boards.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
