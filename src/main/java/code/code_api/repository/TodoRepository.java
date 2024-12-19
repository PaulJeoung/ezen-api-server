package code.code_api.repository;

import code.code_api.domain.Todo;
import code.code_api.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository <Todo, Long>, TodoSearch {

}
