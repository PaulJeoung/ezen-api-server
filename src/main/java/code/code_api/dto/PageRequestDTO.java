package code.code_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data // @Getter + @Setter + @ToString + @RequiredArgsConstructor 들을 포함함
@Builder
// @SuperBuilder // @SuperBuilder >> 상속해서 사용하는 Builder 패턴
@AllArgsConstructor @NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;


}
