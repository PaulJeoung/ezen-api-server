package code.code_api.repository;

import code.code_api.domain.CodeMember;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeMemberRepository extends JpaRepository <CodeMember, String> {

    // 이메일을 확인해서 Role 여부 체크
    @EntityGraph(attributePaths = "memberRoleList")
    @Query ("select m from CodeMember m where m.email = :email")
    CodeMember getWithRoles(@Param("email") String email);

    // nickname으로 찾기
    Optional<CodeMember> getCodeMemberByNickname(String nickname);
}
