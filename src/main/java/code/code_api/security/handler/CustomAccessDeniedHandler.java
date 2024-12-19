package code.code_api.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/*
    에러 처리
    "status": 403,
    "error": "Forbidden",
    "trace": "org.springframework.security.authorization.AuthorizationDeniedException: Access Denied\r\n\tat org.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override // 작성 후 CustomSecurityConfig 클래스의 filterChain () 메서드에 해당 내용을 선언해야 적용됨
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("ERROR", "ERROR_ACCESS_DENIED"));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}
