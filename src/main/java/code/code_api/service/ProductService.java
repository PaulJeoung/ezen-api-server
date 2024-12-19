package code.code_api.service;

import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductService {

    // 리스트 불러오기
    PageResponseDTO<ProductDTO> getlist(PageRequestDTO pageRequestDTO);

    // 상품추가
    Long register(ProductDTO productDTO);

    // 조회기능
    ProductDTO get(Long pno);

    // 수정하기
    void modify(ProductDTO productDTO);
    
    // 삭제하기
    void remove(Long pno);
}