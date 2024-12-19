package code.code_api.config;

import code.code_api.controller.formatter.LocalDateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class CustomServletConfig implements WebMvcConfigurer {
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        // /Controller/formatter 패키지에 있는 format을 출력 하기 위해 다시 재정의
//        log.info("addFormatters() 동작 확인중, 작동하니?");
//        registry.addFormatter(new LocalDateFormatter());
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // CORS 방어로 Ajax을 통해서 데이터를 주고 받을 수 없어서 여기에서 Controller 설정을 추가
//        registry.addMapping("/**") // 모든 경로 허용
//                .maxAge(500) // 연결 시도시, 0.5sec 지연 이후에 연결 종료
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 어떤 방식의 호출을 허용하는지
//                .allowedOrigins("*"); // 허용하는 경로 source , 지금은 다 허용
//    }
}
