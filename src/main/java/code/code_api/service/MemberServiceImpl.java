package code.code_api.service;

import code.code_api.domain.CodeMember;
import code.code_api.domain.MemberRole;
import code.code_api.dto.KakaoUserInfoDTO;
import code.code_api.dto.MemberDTO;
import code.code_api.dto.MemberModifyDTO;
import code.code_api.repository.CodeMemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final CodeMemberRepository codeMemberRepository;
    private final PasswordEncoder passwordEncoder;

    // accessToken을 이용해서 사용자 정보를 가져오고 기존 회원 정보가 있는지 없는지 판단함
    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        KakaoUserInfoDTO kakaoUserInfo = getNicknameFromKakaoAccessToken(accessToken);
        log.info("카카오 유저 정보 : {}",kakaoUserInfo);
        Optional<CodeMember> result = codeMemberRepository.getCodeMemberByNickname(kakaoUserInfo.getNickname());
        // 기존 회원인 경우 DTO로 변환 후 반환
        if(result.isPresent()){
            log.info("이미 회원가입이 되어 있습니다");
            MemberDTO memberDTO = entityToDTO(result.get());
            return memberDTO; // 가입이 필요 없음
        }

        // 새로운 회원인 경우, 비밀번호 임의로 생성
        log.info("회원가입을 시작합니다");
        CodeMember socialMember = makeSocialMember(kakaoUserInfo.getId(), kakaoUserInfo.getNickname());
        codeMemberRepository.save(socialMember);
        MemberDTO memberDTO = entityToDTO(socialMember);
        log.info("가입정보 확인 {} ", memberDTO.getUsername(), memberDTO.getRoleNames());

        return memberDTO; // 가입 프로세스가 필요함
    }

    // getNicknameFromKakaoAccessToken 에서 카카오 정보를 가지고 와서 KakaoDTO에 넣어 놓음
    private KakaoUserInfoDTO getNicknameFromKakaoAccessToken(String accessToken) {
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

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("bodyMap {}", bodyMap);
        log.info("id {}", bodyMap.get("id"));

        LinkedHashMap<String, String> properties = bodyMap.get("properties");
        log.info("nickname {}", properties.get("nickname"));

        String id = String.valueOf(bodyMap.get("id"));
        String nickname = properties.get("nickname");
        
        return new KakaoUserInfoDTO(id, nickname);
    }

    // 해당 소셜로그인을 한 회원이 닉네임을 비교 후 회원이 없는 경우 새로운 회원을 추가할때 패스워드를 임의로 생성
    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();
        for(int i=0; i<10; i++) {
            buffer.append((char) ((int)(Math.random()*55)+65));
        }
        log.info("makeTempPassword() => tempPassword {}", buffer.toString());
        return buffer.toString();
    }

    private CodeMember makeSocialMember(String id, String nickname){
        String tempPassword = makeTempPassword();
        log.info("makeSocialMember() => tempPassword : " + tempPassword);

        // 회원만들기
        CodeMember member = CodeMember.builder()
                .email(id+"@ezen.com")
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }

    // 그냥 만든거 강사님이랑 LinkedHashMap<String, Object> 부분만 다름
    public static void extractIdAndNicknameFromKakao(ResponseEntity<LinkedHashMap> responseEntity) throws Exception {
        // ResponseEntity에서 LinkedHashMap으로 응답 본문을 추출
        LinkedHashMap<String, Object> responseBody = responseEntity.getBody();
        // 'id' 값 추출
        long id = (Long) responseBody.get("id");
        // 'properties' 내의 'nickname' 값 추출
        LinkedHashMap<String, Object> properties = (LinkedHashMap<String, Object>) responseBody.get("properties");
        String nickname = (String) properties.get("nickname");
        // 결과 로그 출력
        log.info("ID: {}", id);
        log.info("Nickname: {}", nickname);
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) { // 소셜 로그인 멤버 비밀번호 수정 + 그외
        Optional<CodeMember> result = codeMemberRepository.getCodeMemberByNickname(memberModifyDTO.getNickname());

        CodeMember member = result.orElseThrow();
        member.setPw(passwordEncoder.encode(memberModifyDTO.getPw()));;
        member.setSocial(false);
        member.setNickname(memberModifyDTO.getNickname());
        codeMemberRepository.save(member);

        // return Map.of("RESULT", "SUCCESS"); 만약에 리턴 줄거면, 메서드 리턴 자료형 Map<String, String>
    }
}
