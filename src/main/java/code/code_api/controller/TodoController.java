package code.code_api.controller;

import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.TodoDTO;
import code.code_api.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // @Controller는 페이지를 주기 위해서 사용, RestController는 api로 쓰기 위함
@RequiredArgsConstructor
@Slf4j
@RequestMapping ("/api/todo") // 상위 주소 알려주는 어노테이션
public class TodoController {

    private final TodoService todoService;

    // 조회 (http://localhost:8080/api/todo/1)
    @GetMapping("/{tno}") //
    public TodoDTO get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    // 리스트
    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list... {}", pageRequestDTO);
        return todoService.getlist(pageRequestDTO);
    }
    
    // 등록하기 post방식 - json 형식으로 받고, return 타입을 json 형식으로 바꿔야 함
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
        log.info("register() 실행시 TodoDTO {}", todoDTO);
        Long tno = todoService.register(todoDTO);
        return Map.of("TNO", tno); // "TNO" 리턴값을 주었기 때문에 나중에 리액트에서 result 리턴을 TNO로 변수를 잡아야 제대로 된 값을 사용할 수 있음!!!
    }

    // 수정하기 Put으로 처리 , return은 json
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDTO todoDTO) {
        // 수정하기 전 안전하게 tno 값을 넣는다
        todoDTO.setTno(tno);
        log.info("modify() 수정{}", todoDTO);
        todoService.modify(todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    // 삭제하기
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno) {
        log.info("remove() 삭제 {}", tno);
        todoService.remove(tno);
        return Map.of("RESULT", "SUCCESS DELETE ID "+tno);
    }
}
