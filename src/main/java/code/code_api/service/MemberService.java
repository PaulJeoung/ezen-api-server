package code.code_api.service;

import code.code_api.domain.CodeMember;
import code.code_api.dto.MemberDTO;
import code.code_api.dto.MemberModifyDTO;

import java.util.stream.Collectors;

public interface MemberService {

    MemberDTO getKakaoMember (String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    // Entity to DTO
    default MemberDTO entityToDTO(CodeMember member) {
        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
        return dto;
    }
}
