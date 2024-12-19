package code.code_api.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder @ToString(exclude = "memberRoleList")
@AllArgsConstructor @NoArgsConstructor
@Getter
public class CodeMember {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social; // 소셜로그인 true, 일반사용자 false

    @ElementCollection(fetch = FetchType.LAZY) // memberRoleList가 실제로 사용될때만 데이터 로드
    @Builder.Default // ROLE 넣을때 NULL이 들어가지 않도록 하기 위해서 
    private List<MemberRole> memberRoleList = new ArrayList<>();
    // ROLE 관련 테이블 생성을 위해서 이곳에서 추가, 제거 함수 구현
    public void addRole(MemberRole memberRole) { // 권한 부여
        memberRoleList.add(memberRole);
    }
    public void clearRole() { // 권한 삭제
        memberRoleList.clear();
    }

    /*
        nickname, pw, social은 변경 가능하게 setter 추가
     */
    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSocial(boolean social) {
        this.social = social;
    }
}
