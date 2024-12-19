package code.code_api.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable // PK가 없음
@Getter
@ToString
@Builder @AllArgsConstructor @NoArgsConstructor
public class ProductImage {

    private String fileName;

    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }

}
