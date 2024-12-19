package code.code_api.service;

import code.code_api.domain.Todo;
import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.TodoDTO;
import code.code_api.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j @RequiredArgsConstructor
public class TodoServiceImpl implements TodoService  {

    private final TodoRepository todoRepository;

    // 등록
    @Override
    public Long register(TodoDTO dto) {
        Todo todo = dtoToentity(dto); // 미리 만들어 놓은 dtoToEntity를 활용
        Todo result = todoRepository.save(todo);
        log.info("register() return Long value {}", result.getTno());
        return result.getTno();
    }
    // 수정
    @Override
    public void modify(TodoDTO dto) {
        Optional<Todo> result = todoRepository.findById(dto.getTno()); // optional get tno_id
        Todo todo = result.orElseThrow(); // tno_id of todo object return
        todo.setTitle(dto.getTitle());
        todo.setContent(dto.getContent());
        todo.setComplete(dto.isComplete());
        todo.setDueDate(dto.getDueDate());
        todoRepository.save(todo);
    }
    // 삭제
    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }
    // 조회
    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow(); // if (isPresent) 대신 사용
        return entityToDTO(todo);
    }

    // 페이징 처리
    @Override
    public PageResponseDTO<TodoDTO> getlist(PageRequestDTO pageRequestDTO){

        Page<Todo> result = todoRepository.search1(pageRequestDTO);
        List<TodoDTO> dtoList = result.get().map(todo -> entityToDTO(todo)).collect(Collectors.toList());

        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();
        log.info(responseDTO.getDtoList().toString());

        return responseDTO;
    }


}
