package code.code_api.security;

import code.code_api.domain.CodeMember;
import code.code_api.dto.MemberDTO;
import code.code_api.repository.CodeMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService { // Spring Security 인증 처리를 이곳에서 UserDetailService 구현체를 활용

    private final CodeMemberRepository codeMemberRepository;

    @Override // loadUserByUsername() 에서 사용자 정보를 조회 하고 사용자의 인증과 권한을 처리
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername() - 시큐리티가 사용자 정보 조회를 처리 하는지 확인 => username : {} ", username);

        CodeMember member = codeMemberRepository.getWithRoles(username);
        if (member==null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList()
                        .stream()
                        .map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
        log.info("로그인한 멤버 {}",memberDTO);
        return memberDTO;
    }

}
