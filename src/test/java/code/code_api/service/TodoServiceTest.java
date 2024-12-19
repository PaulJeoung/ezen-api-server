package code.code_api.service;

import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.TodoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TodoServiceTest {

    @Autowired TodoService todoService;

    @Test
    void 조회() {
        Long tno = 50L;
        log.info("50번 조회 {}", todoService.get(tno));
    }

    @Test
    void 등록() {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title 추가")
                .content("content 하나더용")
                .complete(true)
                .dueDate(LocalDate.now())
                .build();
        log.info("추가 {}", todoService.register(todoDTO));
    }

    @Test
    void 페이징리스트() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        log.info("페이징링스트의 기본값 {}", todoService.getlist(pageRequestDTO));
    }

    @Test
    void 페이징리스트11번() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .build();
        log.info("페이징링스트의 기본값 {}", todoService.getlist(pageRequestDTO));
    }

}