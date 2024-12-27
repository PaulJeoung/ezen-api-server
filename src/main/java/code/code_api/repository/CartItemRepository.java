package code.code_api.repository;

import code.code_api.domain.CartItem;
import code.code_api.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 특정 사용자의 아이디를 통해서 해당 사용자의 모든 장바구니 아이템을 조회 하는 기능
    @Query("select " +
            " new code.code_api.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName)" +
            " from " +
            " CartItem ci inner join Cart c on ci.cart = c" +
            " left join Product p on ci.product = p" +
            " left join p.imageList pi" +
            " where " +
            " c.owner.email = :email and pi.ord = 0" +
            " order by ci.cino desc")
    public List<CartItemListDTO> getItemOfCartDTOByEmail(@Param("email") String email);

    // 사용자의 이메일과 상품 번호로 해당 장바구니 아이템을 알아내는 기능
    @Query("select ci" +
            " from CartItem ci inner join Cart c on ci.cart = c" +
            " where " +
            " c.owner.email = :email and ci.product.pno = :pno")
    public CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

    // 장바구니 아이템 번호를 이용해 장바구니를 알고 싶을때
    @Query("select c.cno " +
            " from Cart c inner join CartItem ci on ci.cart = c" +
            " where " +
            " ci.cino = :cino")
    public Long getCartFromItem(@Param("cino") Long cino);

    // 특정 장바구니의 번호만으로 해당 장바구니의 모든 장바구니 아이템을 조회하는 기능
    @Query("select " +
            " new code.code_api.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName)" +
            " from " +
            " CartItem ci inner join Cart c on ci.cart = c" +
            " left join Product p on ci.product = p" +
            " left join p.imageList pi" +
            " where " +
            " c.cno = :cno and pi.ord = 0" +
            " order by ci.cino desc")
    public List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);

}
