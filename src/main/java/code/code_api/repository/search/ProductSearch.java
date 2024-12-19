package code.code_api.repository.search;

import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList (PageRequestDTO pageRequestDTO);
}
