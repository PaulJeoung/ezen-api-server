package code.code_api.config;

import code.code_api.security.filter.JWTCheckFilter;
import code.code_api.security.handler.APILoginFailHandler;
import code.code_api.security.handler.APILoginSuccessHandler;
import code.code_api.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration // 설정파일 선언
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    // filterChain 아래에 정의한 bean들을 사용 하기 위서 해당 filterChain에서 사용할 메소드를 선언
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info(".... security config - filterChain()");

        // corsConfigurationSource() CORS 정잭 사용 메서드 선언
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        
        // CSRF 정책 미사용 메서드 선언
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        
        // 뷰가 없어서 임시로 시큐리티에서 제공하는 LoginForm을 사용
        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());
            config.failureHandler(new APILoginFailHandler());
        });

        // 사용자ID, Password 를 검증하는 필터 전에 JWTCheckFilter()로 만든 필터를 먼저 동작 함
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 체크

        // 유효시간이 지나지 않았지만 권한이 없는 사용자가 가진 토큰을 사용하는 경우
        http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // CORS 정책
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 origin을 허용
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // 허용할 HTTP 헤더 설정
        
        // 클라이언트가 **자격 증명 (쿠기, 인증정보 등)**을 포함한 요청을 보낼 수 있도록 허용
        configuration.setAllowCredentials(true); // true로 설정되면, 서버는 자격 증명 있는 요청을 신뢰
        // URL 패턴에 따라 CorsConfigration을 매핑 할수 있는 객체를 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // "/**" 로 지정하여 모든 URL 경로에 대해 이 CORS 정책을 적용

        return source;
    }
}
