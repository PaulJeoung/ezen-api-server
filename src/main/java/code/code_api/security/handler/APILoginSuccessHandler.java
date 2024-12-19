package code.code_api.security.handler;

import code.code_api.dto.MemberDTO;
import code.code_api.util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder; // JSON 포맷팅을 위한 클래스 추가
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공 후 인증 정보 : \n{}", authentication);
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();

        Map<String, Object> claims = memberDTO.getClaims(); // 사용자의 클레임 데이터를 가져옴

        String accessToken = JWTUtil.generateToken(claims, 10); // JWTUtil 을 이용해 accessToken을 생성
        String refreshToken = JWTUtil.generateToken(claims, 60 * 24); // JWTUtil 을 이용해 refreshToken 생성
        claims.put("accessToken",accessToken);
        claims.put("refreshToken",refreshToken);

        /*
        // JSON 객체를 포맷팅하여 로그로 출력 (GsonBuilder로 예쁘게 출력)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(claims);
        log.info("JSON 응답 : \n{}", jsonStr);  // 예쁘게 포맷된 JSON 출력

        // HTTP 응답으로 반환
        response.setContentType("application/json; charset=UTF-8");

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.println(jsonStr);  // 응답 본문에 JSON 출력
        } catch (IOException e) {
            log.error("응답 스트림을 작성하는 중 오류 발생", e);
        }
         */
        
        Gson gson =  new Gson(); // json 응답을 위해서 GSON 라이브러리를 이용해 JSON 형태로 변환
        String jsonStr = gson.toJson(claims);
        log.info("JSON 응답 : \n{}", jsonStr);
        // http 응답으로 반환
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();

    }
}
