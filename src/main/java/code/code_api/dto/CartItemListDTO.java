package code.code_api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {
    // 누구의 Cart : nickname
    // 카트번호, 상품번호, 상품명, 가격, 수량, 상품이미지

    private Long cino;
    private int qty;
    private Long pno;
    private String pname;
    private int price;
    private String imageFile;

    public CartItemListDTO(Long cino, int qty, Long pno, String pname, int price, String imageFile) {
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
    /*
        ci1_0.cino,
        ci1_0.cart_cno,
        ci1_0.product_pno,
        ci1_0.qty,
        c1_0.cno,
        c1_0.codemember_email,
        p1_0.pno,
        p1_0.del_flag,
        p1_0.pdesc,
        p1_0.pname,
        p1_0.price
     */
}
