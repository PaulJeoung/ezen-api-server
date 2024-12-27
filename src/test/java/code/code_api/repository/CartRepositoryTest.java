package code.code_api.repository;

import code.code_api.domain.Cart;
import code.code_api.domain.CartItem;
import code.code_api.domain.CodeMember;
import code.code_api.domain.Product;
import code.code_api.dto.CartItemListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
class CartRepositoryTest {

    @Autowired CartRepository cartRepository;
    @Autowired CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    void 장바구니만들기() {
        String email = "user7@ezen.com";
        Long pno = 4L;
        int qty = 2;
        // 기존에 사용자의 장바구니 아이템이 있는지 확인
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if(cartItem != null){
            cartItem.setQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }
        // 장바구니 아이템이 없었다면 장바구니 유무 확인
        Optional<Cart> result = cartRepository.getCartOfMember(email);
        Cart cart = null;
        if(result.isEmpty()){
            // 카트를 만든다 => 멤버를 만든다 => 새로운 카트를 만든다
            CodeMember member = CodeMember.builder()
                    .email(email)
                    .build();
            Cart tempCart = Cart.builder()
                    .owner(member)
                    .build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }
        log.info("카트는 {}", cart);
        // 상품을 널어주기
        Product product = Product.builder()
                .pno(pno)
                .build();
        CartItem cartGo = CartItem.builder()
                .product(product)
                .cart(cart)
                .qty(qty)
                .build();
        cartItemRepository.save(cartGo);
    }
    
    // 장바구니 아이템 수량 수정
    @Test
    @Commit
    void 장바구니아이템수량수정(){
        Long cino = 1L;
        int qty = 10;
        Optional<CartItem> result = cartItemRepository.findById(cino);
        CartItem cartItem = result.orElseThrow();
        cartItem.setQty(qty);
        cartItemRepository.save(cartItem);
    }
    
        /*
공유빈 (진행율 : ✦✦✧✧ 50%)
- 상품테이블 백엔드 CRUD 프로토타입 구현 : 100%
- 상품테이블 프론트엔드 리스트 프로토타입 구현 : 10%
한형준 (진행율 : 완료)
- 장바구니테이블 백엔드 CRUD 프로토타입 구현 : 100%
- 장바구니테이블 프론트엔드 리스트 프로토타입 구현 : 100%
윤수명 (진행율 : ✦✦✧✧ 50%)
- 리뷰테이블 백엔드 CRUD 프로토타입 구현 : 100%
- 리뷰테이블 프론트엔드 리스트 프로토타입 구현 : 10%
설재훈 (진행율 : ✦✦✦✧ 70%)
- Q&A테이블 백엔드 CRUD 프로토타입 구현 : 90%
- Q&A테이블 프론트엔드 리스트 프로토타입 구현 : 40%
- Github repository 세팅 : 100%
김준범 (진행율 : ✦✦✧✧ 50%)
- 좋아요테이블 백엔드 CRUD 프로토타입 구현 : 90%
- 좋아요테이블 프론트엔드 리스트 프로토타입 구현 : 10%
정병오 (진행율 : ✦✦✦✧ 70%)
- 공지사항테이블 백엔드 CRUD 프로토타입 구현 : 90%
- 공지사항테이블 프론트엔드 리스트 프로토타입 구현 : 60%

        [ProtoType] Q&A B/E CRUD, F/E 데이터로딩	설재훈	우선순위 1	✦✧✧✧ 60%
        [ProtoType] 공지사항 B/E CRUD, F/E 데이터로딩	Lidia J	우선순위 1	✦✧✧✧ 60%
        [ProtoType] 좋아요 B/E CRUD, F/E 데이터로딩	마르셀로 김	우선순위 1	✦✧✧✧ 50%
     */
    // 현재 사용자의 장바구니 아이템 목록 테스트
    @Test
    void 장바구니아이템목록보기(){
        String email = "user7@ezen.com";
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemOfCartDTOByEmail(email);
        for (CartItemListDTO dto : cartItemList) {
            log.info("장바구니 아이템 목록 {}", dto.toString());
            log.info("\n장바구니 아이템 목록 {}", dto);
        }

    } 

    @Test
    void 특정사용자의아이디를통해서해당사용자의모든장바구니아이템을조회하는기능(){
        String email = "user8@ezen.com";
        cartItemRepository.getItemOfCartDTOByEmail(email);
    }

    @Test
    void 사용자의이메일과상품번호로해당장바구니아이템을알아내는기능(){
        String email = "user8@ezen.com";
        Long pno = 10L;
        cartItemRepository.getItemOfPno(email, pno);
    }
    @Test
    void 장바구니아이템번호를이용해장바구니조회(){
        Long pno = 2L;
        cartItemRepository.getCartFromItem(pno);
    }

    @Test
    void 특정장바구니의번호만으로해당장바구니의모든장바구니아이템을조회하는기능(){
        Long cno = 3L;
        cartItemRepository.getItemsOfCartDTOByCart(cno);
    }


}