package code.code_api.service;

import code.code_api.dto.MemberDTO;

public interface MemberService {

    MemberDTO getKakaoMember (String accessToken);

}
