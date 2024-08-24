package edu.example.restz.repository;

import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import edu.example.restz.repository.search.TodoSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
    // extends <>, "TodoSearch"는 querydsl 로 정의한 메소드

    // 아래에 있는 내용들은 @Query
    @Query("SELECT t FROM Todo t WHERE t.mno = :mno")
    Optional<TodoDTO> getTodoDTO(@Param("mno") Long mno);

    @Query("SELECT t FROM Todo t ORDER BY t.mno DESC") // 'mno'
    Page<Todo> ListAll(Pageable pageable);
}
