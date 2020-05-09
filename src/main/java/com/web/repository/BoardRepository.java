package com.web.repository;

import com.web.domain.enums.Board;
import com.web.domain.enums.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Board findByUser(User user);
}
