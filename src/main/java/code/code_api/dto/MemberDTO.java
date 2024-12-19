package code.code_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
@ToString
public class MemberDTO extends User {

    private String email, pw, nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String email, String pw, String nickname, Boolean social, List<String> roleNames) { // secure security 를 사용할때는 권한을 객체로 전달 하고 받아야 함
        super(email, pw, roleNames.stream().map(str ->
                new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList())); // super로 상속

        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() { // JWT 문자열을 만들어서 데이터를 주고 받음 (문자열 속 내용물을 Claims 라고 함)
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }

}
