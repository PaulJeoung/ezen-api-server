package code.code_api.service;

import code.code_api.domain.Product;
import code.code_api.domain.ProductImage;
import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;
import code.code_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // 목록데이터 조회
    @Override
    public PageResponseDTO<ProductDTO> getlist(PageRequestDTO pageRequestDTO) {
        // 페이지 목록 만들기
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );
        /*
        [ Product (...), // 0번지
          ProductImage (...) ] // 1번지
         */
        // 리포지토리에서 목록 가져오기 (대표이미지 1개)
        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDTO> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];
            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .price(product.getPrice())
                    .pdesc(product.getPdesc())
                    .build();
            String imageFileName = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageFileName));
            return productDTO;
        }).toList();// collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    // 상품 추가
    @Override
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity((productDTO));
        log.info("새로운 상품 : {} ", product);
        log.info("새로운 상품의 이미지 이름 : {} ", product.getImageList());
        Long pno = productRepository.save(product).getPno();
        return pno;
    }
    
    // dto를 받아서 entity로 변환 하는 코드
    private Product dtoToEntity(ProductDTO productDTO){
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if (uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }
        uploadFileNames.stream().forEach(uploadName -> {
            product.addImageString(uploadName);
        });

        return product;
    }

    // entity To DTO
    private ProductDTO entityToDTO(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();
        if (imageList == null || imageList.isEmpty()){
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(
                productImage -> productImage.getFileName()
        ).toList();
        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
    }

    // 상품 조회 (단건)
    @Override
    public ProductDTO get(Long pno) {
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDTO productDTO = entityToDTO(product);
        return productDTO;
    }

    // 상품 수정
    @Override
    public void modify(ProductDTO productDTO) {
        // 1개의 상품을 조회
        Optional<Product> result = productRepository.selectOne(productDTO.getPno());
        Product product = result.orElseThrow();
        // 새로 들어온 내용으로 수정
        product.setPname(productDTO.getPname());
        product.setPdesc(productDTO.getPdesc());
        product.setPrice(productDTO.getPrice());
        product.setDelFlag(productDTO.isDelFlag());
        // 무조건 지운다 (파일이름)
        product.clearList();
        // 입력된 이미지 파일 이름을 가져옴
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        // 파일이름 업로드
        if(uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.stream().forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }
        // 저장
        productRepository.save(product);
    }

    // 상품 삭제
    @Override
    public void remove(Long pno) {
        // productRepository.updateToDelete(pno, true);
        productRepository.deleteById(pno);
    }
}
