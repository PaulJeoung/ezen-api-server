package code.code_api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"cart", "product"}) // 제외할 테이블 2개이상은 {}
@Table(name = "tbl_cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cino;

    @ManyToOne
    @JoinColumn(name = "product_pno")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_cno")
    private Cart cart;

    private int qty; // 수량

    public void setQty(int qty) {
        this.qty = qty;
    }
}
