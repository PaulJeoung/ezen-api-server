package code.code_api.repository.search;

import code.code_api.domain.QTodo;
import code.code_api.domain.Todo;
import code.code_api.dto.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j

public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {
        log.info("search1() 동작하나?");
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo> query = from(todo);
        // query.where(todo.title.contains("1"));

        // 페이징 처리 추가
        PageRequest pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());
        this.getQuerydsl().applyPagination(pageable, query);
        List<Todo> list = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(list, pageable, total);
        // pageable docs
        // https://docs.spring.io/spring-data/commons/docs/2.6.4/api/index.html?org/springframework/data/domain/PageImpl.html
    }
}
