package code.code_api.repository.search;

import code.code_api.domain.Todo;
import code.code_api.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);

}
