package code.code_api.service;

import code.code_api.dto.MemberDTO;
import code.code_api.repository.CodeMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final CodeMemberRepository codeMemberRepository;

    // accessToken을 이용해서 사용자 정보를 가져오고 기존 회원 정보가 있는지 없는지 판단함
    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        getNicknameFromKakaoAccessToken(accessToken);

        return null;
    }

    // getNicknameFromKakaoAccessToken
    private void getNicknameFromKakaoAccessToken(String accessToken) {
        // 카카오 호출
        String kakaoFetchUserURL = "https://kapi.kakao.com/v2/user/me";
        if(accessToken == null){
            throw new RuntimeException("Access Token is Null");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // 실제 호출
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoFetchUserURL).build();
        log.info(uriBuilder.toString());

        // 응답값 확인 (LinkedHashMap 형태로 나옴)
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(),
                HttpMethod.GET,
                entity,
                LinkedHashMap.class
        );
        log.info("Response : {}", response);


        // DB 조회
        
        
        // 없으면 이메일 만들어서 저장



    }
}
