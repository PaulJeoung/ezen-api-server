package code.code_api.repository;

import code.code_api.domain.CodeMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static code.code_api.domain.MemberRole.*;

@SpringBootTest
@Slf4j
class CodeMemberRepositoryTest {

    @Autowired private CodeMemberRepository codeMemberRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() {
        for (int i=0; i<=10; i++) {
            CodeMember member = CodeMember.builder()
                    .email("user"+i+"@ezen.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("user"+i)
                    .social(false)
                    .build();
            member.addRole(USER);
            if (i>=6) member.addRole(MANAGER);
            if (i>=8) member.addRole(ADMIN);
            codeMemberRepository.save(member);
        }
    }

    @Transactional
    @Test
    void 회원조회() {
        CodeMember result = codeMemberRepository.getWithRoles("user9@ezen.com");
        log.info("찾은 회원 정보   : {}", result);
        log.info("회원의 ROLE 정보 : {}", result.getMemberRoleList());
    }

}