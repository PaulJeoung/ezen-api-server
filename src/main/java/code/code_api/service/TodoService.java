package code.code_api.service;

import code.code_api.domain.Todo;
import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.TodoDTO;

public interface TodoService {
    // 조회기능
    TodoDTO get(Long tno);

    // 등록하기
    Long register(TodoDTO dto);

    // 수정하기
    void modify(TodoDTO dto);

    // 삭제하기
    void remove(Long tno);

    // 리스트 불러오기
    PageResponseDTO<TodoDTO> getlist(PageRequestDTO pageRequestDTO);

    // Entity를 DTO로 변환 (F/E 조회 시)
    default TodoDTO entityToDTO(Todo todo){ // java8 부터 default 사용가능
        TodoDTO todoDTO = TodoDTO.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .complete(todo.isComplete())
                .dueDate(todo.getDueDate())
                .build();
        return todoDTO;
    }

    // DTO를 Entiry로 변환 (저장, 업데이트시)
    default Todo dtoToentity(TodoDTO todoDTO) {
        Todo todo = Todo.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .complete(todoDTO.isComplete())
                .dueDate(todoDTO.getDueDate())
                .build();
        return todo;
    }
}
