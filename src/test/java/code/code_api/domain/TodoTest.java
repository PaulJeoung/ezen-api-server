package code.code_api.domain;

import code.code_api.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class TodoTest {

    @Autowired TodoRepository todoRepository;
    // 유니테스트 코드 만들기 : (만들려는 자바로 가서 해당 함수에다가 )Control+Shift+T

    @Test
    void 할일추가() { // builder pattern을 이용한 테스트 데이터 작성
        Todo todo = Todo.builder()
                        .title("title")
                        .content("content...")
                        .dueDate(LocalDate.now())
                        .build();
        todoRepository.save(todo);
    }

    @Test
    void 할일Hundred() {
        for (int i=1; i<=100; i++) {
            Todo todo = Todo.builder()
                    .title("title : " + i)
                    .content("content...  ==> " + i)
                    .dueDate(LocalDate.now())
                    .build();
            todoRepository.save(todo);
        }
    }

    @Test
    void 조회번호33번() {
        Long tno = 33L;
        Optional<Todo> findTodo = todoRepository.findById(tno);
        if (findTodo.isPresent()){
            assertEquals(33L, findTodo.get().getTno());
        }
    }

    @Test
    void 수정번호33번() {
        Long tno = 33L;
        Optional<Todo> findTodo = todoRepository.findById(tno);
        Todo todo = findTodo.orElseThrow();
        todo.setTitle("title modify 33");
        todo.setContent("content modify ==> 33");
        todo.setComplete(true);
        todo.setDueDate(LocalDate.of(2024, 12, 4));
        todoRepository.save(todo);
    }

    @Test
    void 삭제번호99번() {
        Long tno = 101L;
        todoRepository.deleteById(tno);
    }

    @Test
    void 페이징테스트() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        Page<Todo> result = todoRepository.findAll(pageable);
        log.info("결과 {}" , result.getTotalElements());
        result.getContent().stream().forEach(todo -> log.info("데이터 {} : ", todo));
    }

//    @Test
//    void 검색1() {
//        todoRepository.search1(pageRequestDTO);
//    }

}