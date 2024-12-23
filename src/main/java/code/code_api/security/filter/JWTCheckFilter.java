package code.code_api.security.filter;

import code.code_api.dto.MemberDTO;
import code.code_api.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/*
    HTTP요청이 애플리케이션으로 들어오면 OncePerRequestFilter가 요청을 가로챔
    doFilterInternal() 메서드가 호출되어 필터링 로직이 실행됨
    필터링 작업을 마친 후 filterChain.doFilter 호출 하면 다음 필터 또는 컨트롤러가 요청을 처리 함
    react kapi key :  293596f68efad223adcc7b78d14d7689
    react kapi secret key : NqlGFbzAFtTwlQVc9CghCOvfhoIHLYDB
*/
@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override // 검증 제외할 필터 (url)
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if(request.getMethod().equals("OPTIONS")) { //
            return true;
        }

        String path = request.getRequestURI();
        // return false <-- 체크함 , return true <-- 체크안함

        if(path.startsWith("/api/member/")){ // member 로그인 필터 해제
            return true;
        }

        if(path.startsWith("/api/products/view/")){ // 이미지 조회 경로를 체크 하지 않는다면,
            return true;
        }

        return false; // super.shouldNotFilter(request);
    }

    @Override // 검증할 필터 (url)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("\n검증할 필터를 확인 중입니다... (url 체크 중...)");
        String authHeaderStr = request.getHeader("Authorization"); // Header 정보를 가져옴

        try {
            String accessToken = authHeaderStr.substring(7); // Beaerer_ 공백까지 7글자를 잘라냄
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);
            // filterChain.doFilter(request, response);

            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("\n 멤버 : {}", memberDTO);
            log.info("\n 멤버권한 : {}", memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.info("\n필터 검증이 완료 되었습니다!!");
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.info("\n필터 검증 도중 에러가 발견 되었습니다  ERROR : {}", e.getMessage());
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("ERROR", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }

    }
}
