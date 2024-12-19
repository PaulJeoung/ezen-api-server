package code.code_api.service;

import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
class ProductServiceTest {

    @Autowired ProductService productService;

    @Test
    void 상품조회() {
        // 기본 1페이지, 10개
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ProductDTO> result = productService.getlist(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info("상품리스트 : {}", dto.getPname()));
    }

    @Test
    void 상품저장() {
        ProductDTO productDTO = ProductDTO.builder()
                .pname("NEW ARRIVAL")
                .pdesc("2024년 겨울신상")
                .price(150000)
                .build();
        productDTO.setUploadFileNames(
                List.of(
                        UUID.randomUUID() + "_" + "new1.jpg",
                        UUID.randomUUID() + "_" + "new2.jpg"
                )
        );
        productService.register(productDTO);

    }

    @Test
    void 상품1번가져오기() {

        ProductDTO productDTO = productService.get(14L);
        log.info("찾은 상품 {} {}", productDTO.getPno(), productDTO.getPname());
        log.info("찾은 상품 이미지 {}", productDTO.getUploadFileNames());
    }

}