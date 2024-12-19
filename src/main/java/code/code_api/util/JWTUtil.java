package code.code_api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtil {
    /*
        JWT 키 서명을 생성 할때 비밀키를 사용하며, 최소 256비트(32자) 필요
        HMAC-SHA 알고리즘 사용
        1. JWT 문자열 생성을 위한 generateToken() 메서드선언
        - valueMap (JWT의 claim, 사용자 정보 데이터), min (사용할 시간 데이터)
        2. validateToken() 메서드 선언
        - 토큰의 클레임 데이터를 저장할 변후, 클레임은 토큰에 담긴 정보(key-value) 로 생성
     */
    private static String key = "1234567890123456789012345678901234567890";
    private static Keys keys;

    public static String generateToken(Map<String, Object> valueMap, int min) { // Object를 받아서 stream으로 반환 해야 됨
        SecretKey key = null;
        try {
            key = keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT")) // JWT의 헤더를 설정
                .setClaims(valueMap) // 클레임 설정
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발급시간
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료시간
                .signWith(key) // HMAC-SHA 알고리즘으로 서명을 만듦
                .compact(); // JWT를 직렬화 하여 최종 문자열로 반환
        return jwtStr;
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;
        SecretKey key = null;
        try {
            key = keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8")); // JWT 생성 시 사용한 키와 동일
            claim = Jwts.parserBuilder() // JWT 문자열을 파싱할 객체 빌드
                    .setSigningKey(key) // JWT 서명을 검증하기 위해 사용할 비밀키 설정
                    .build()
                    .parseClaimsJws(token) // 입력받은 JWT문자열을 파싱해 유효성을 확인, 서명이 유효한지, 토큰이 만료되지 않았는지 확인
                    .getBody(); // 검증이 성공하면 토큰의 페이로드부분에 포함된 클레임 데이터를 반환
        }  catch (MalformedJwtException malformedJwtException){
            throw new CustomJWTException("MalFormed"); // 토큰이 잘못된 형식으로 작성된 경우
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("Expired"); // 토큰이 만료 되었거나 만료시간이 잘못된 경우
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid"); // JWT 처리 중 클레임 값이 특정 검증 조건을 충족하지 않을때
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JWTError"); // JWT 생성, 검증, 또는 파싱 과정에서 발생하는 문제들
        } catch (Exception e) {
            throw new CustomJWTException("Error"); // 그외의 경우
        }
        return claim;
    }

}
