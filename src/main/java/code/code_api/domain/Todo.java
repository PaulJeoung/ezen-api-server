package code.code_api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter // 이제 DAO에서 @Setter는 사용하지 않음
@ToString // 대신 toString 만들어줌
@Builder // builder pattern 적용을 위해 선언
@AllArgsConstructor // 모든 파라미터를 적용한 생성자
@NoArgsConstructor // 파라미터가 없는 생성자
public class Todo {  // 유니테스트 코드 만들기 : (만들려는 자바로 가서 해당 함수에다가 )Control+Shift+T

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String title;

    private String content;

    private boolean complete;

    private LocalDate dueDate;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
