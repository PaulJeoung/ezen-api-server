package code.code_api.controller;

import code.code_api.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;
    @GetMapping("/api/member/kakao")
    public String[] getMemberFromKakao(@RequestParam("accessToken") String accessToken){
        log.info("react에서 가져온 access Token {} ", accessToken);

        memberService.getKakaoMember(accessToken);

        return new String[]{"AAA", "BBB", "VVV"};
        /*
            Response : <200 OK OK,{id=3846982924, connected_at=2024-12-23T06:29:36Z, properties={nickname=정병오 🙂}
                    , kakao_account={profile_nickname_needs_agreement=false, profile={nickname=정병오 🙂, is_default_nickname=false}}},
                    [Date:"Mon, 23 Dec 2024 07:35:05 GMT",
                    Server:"Apache", Access-Control-Allow-Origin:"*",
                     Access-Control-Allow-Methods:"GET, POST, PUT, DELETE, OPTIONS",
                     Access-Control-Allow-Headers:"Content-Type,X-Requested-With,Accept,Authorization,Origin,KA,Cache-Control,Pragma",
                     X-Request-ID:"dce1e547-931f-4d6b-a4d3-22b07d8c2028", Quota-Type:"INC_AND_CHECK", Content-Type:"application/json;charset=UTF-8",
                     Content-Length:"225", Keep-Alive:"timeout=10, max=500", Connection:"Keep-Alive"]>
         */

    }

}
