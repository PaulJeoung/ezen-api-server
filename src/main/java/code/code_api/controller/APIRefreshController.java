package code.code_api.controller;

import code.code_api.util.CustomJWTException;
import code.code_api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class APIRefreshController {
    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, @RequestParam("refreshToken") String refreshToken) {

        if(refreshToken == null) { // refreshToken이 없는 경우
            throw new CustomJWTException("NULL_REFRESH");
        } else if(authHeader == null || authHeader.length() < 7) { // authHeader가 없거나 Bearer 가 7자가 안되게 들어오는 경우
            throw new CustomJWTException("INVALID_STRING");
        }
        // Access 토큰이 만료 되지 않았다면, 그대로 사용
        String accessToken = authHeader.substring(7);
        if(checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // refresh 토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh.... claims : {}", claims);

        // 새로운 accessToken 발행
        String newAccessToken = JWTUtil.generateToken(claims, 10);

        // refreshToken의 시간을 검사해서 다시 발행 할지, 아닐지 결정
        String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60*24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
    
    private boolean checkExpiredToken(String accessToken) { // 만료되었지는 검사, true면 만료, false면 아직 만료 되지 않음
        try {
            JWTUtil.validateToken(accessToken);
        } catch (CustomJWTException e) {
            if(e.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }

    private boolean checkTime(Integer exp) { // 시간이 1시간 미만으로 남았는지 체크, true를 반환하면 1시간도 안남음
        // JWT는 UNIX timestamp 사용함 (초단위, but 자바는 ms 단위라서 1000 곱해서 맞춰줌
        Date expDate = new Date((long) exp * (1000));

        // 현재시간과의 차이 계산 후 분 단위 계산
        long gap = expDate.getTime() - System.currentTimeMillis();
        long leftMin = gap / (1000 * 60);

        return leftMin < 60;
    }
}
