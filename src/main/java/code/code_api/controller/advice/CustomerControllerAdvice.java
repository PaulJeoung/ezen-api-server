package code.code_api.controller.advice;

import code.code_api.util.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

// NoSuchElementException 등의 500(서버에러) 처리 하기 위해 생성
// NOT_FOUND(404), NOT_ACCEPTABLE(406) 등의 클라이언트 에러 처리로 리턴
@RestControllerAdvice
public class CustomerControllerAdvice {
    
    // 없는 번호 조회시 에러 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notExist(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "페이지를 찾을 수 없습니다")); // e.getMessage()
    }

    // List 쿼리시에 페이지 번호 오류
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notExist(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("meg", e.getMessage())); // "잘못된 List로 조회 되었습니다"
    }

    // API RefreshController에서 에러가 발생하면 CustomJWTException을 반환함
    @ExceptionHandler(CustomJWTException.class)
    public ResponseEntity<?> handleJWTException(CustomJWTException e) {
        String msg = e.getMessage();
        return ResponseEntity.ok().body(Map.of("error", msg));
    }

}
