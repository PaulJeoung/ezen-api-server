package code.code_api.repository;

import code.code_api.domain.Product;
import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void 이미지저장() {
        Product product = Product.builder()
                .pname("볍신 by 모란시장에디션")
                .price(49900)
                .pdesc("모란시장 동편 주차장 버스정류장 앞에 불법 주차된 스타렉스에서 이동 판매하는 리미티드 에디션")
                .delFlag(false)
                .build();
        productRepository.save(product);
    }

    @Test
    void 상품추가() {
        for (int i=0; i<11; i++){
            Product product = Product.builder()
                    .pname("This is Product - "+i)
                    .price(19900*i)
                    .pdesc("This Product content description... "+i)
                    .build();
            product.addImageString(UUID.randomUUID().toString() + "_" + "product_image"+(i*11)+".jpg");
            product.addImageString(UUID.randomUUID().toString() + "_" + "product_image"+(i*12)+".jpg");
            productRepository.save(product);
            log.info("저장완료 {}", product.getPname());
        }
    }

    @Transactional
    @Test
    void 상품조회1() {
        Optional<Product> result = productRepository.findById(4L);
        Product product = result.orElseThrow();
        log.info("결과 : {}", product.getPname());
        log.info("이미지 결과 : {}", product.getImageList());
    }

    @Test
    void 상품조회2() {
        Optional<Product> result = productRepository.selectOne(4L);
        Product product = result.orElseThrow();
        log.info("결과 : {}", product.getPname());
        log.info("이미지 결과 : {}", product.getImageList());
    }

    @Commit
    @Transactional
    @Test
    void 상품삭제() {
        productRepository.updateToDelete(3L, true);
        Optional<Product> result = productRepository.findById(3L);
        Product product = result.orElseThrow();
        log.info("결과 : {}", product.getPname());
        log.info("dflag : {}", product.isDelFlag());

    }

    @Test
    void 상품수정() {
        Optional<Product> result = productRepository.selectOne(11L);
        Product product = result.orElseThrow();
        product.setPname("해당 상품은 판매중지");
        product.setPdesc("재고 부족으로 인한 판매 중지");
        product.setPrice(1);
        product.clearList();
        product.addImageString(UUID.randomUUID().toString() + "_" + "newImage1.jpg");
        product.addImageString(UUID.randomUUID().toString() + "_" + "newImage2.jpg");
        product.addImageString(UUID.randomUUID().toString() + "_" + "newImage3.jpg");
        productRepository.save(product);
    }

    @Test
    void 상품리스트조회() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Test
    void 상품검색() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        log.info("페이지 정보 {} ",pageRequestDTO.toString());
        productRepository.searchList(pageRequestDTO);

    }
}