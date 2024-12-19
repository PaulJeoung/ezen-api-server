package code.code_api.controller;

import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;
import code.code_api.service.ProductService;
import code.code_api.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping ("/api/products")
public class ProductController {

    private final CustomFileUtil customFileUtil;
    private final ProductService productService;

    // 파일 업로드 => 저장
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {
        log.info("register() value {}", productDTO);
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = customFileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames); // 저장
        // log.info("productDTO.setUploadFileNames{}", uploadFileNames);
        Long pno = productService.register(productDTO); // 서비스 호출
        // 지연 테스트를 위해서 sleep (로딩 모달 확인용)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Map.of("result", pno);
    }

    // 상품 이미지 조회
    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("filename") String filename) {
        return customFileUtil.getFile(filename);
    }

    // 상품목록조회
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')") // JWTCheckFilter 클래스의 doFilterInternal() 에 처리 할 내용을 넣어준다. 여기에서는 역할만 설정
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getlist(pageRequestDTO);
    }

    // 상품 저장
    @PostMapping("/list")
    public Long save(ProductDTO productDTO) {
        return productService.register(productDTO);
    }

    // 상품 조회 (단건)
    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    // 상품 수정
    @PutMapping("/{pno}")
    public Map<String, String> modify (@PathVariable("pno") Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);
        // 기존 정보 선언
        ProductDTO oldProductDTO = productService.get(pno);
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        // 새로 업로드 해야 하는 파일과 이미지 네임
        List<MultipartFile> files = productDTO.getFiles(); // 실제 이미지
        List<String> currentUploadFileNames = customFileUtil.saveFiles(files); // 새로 업로드 해야 하는 파일의 이름
        // 유지되는 파일과 삭제/수정 되어야 하는 파일
        List<String> uploadedFileNames = productDTO.getUploadFileNames();
        // 유지되는 파일 + 새로 업로드 될 파일 목록 만들기
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }
        // 수정 (업데이트) e.g) ABC 중 C삭제, D는 신규 -> ABD
        productService.modify(productDTO);
        if(oldFileNames != null && !oldFileNames.isEmpty()){
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            //실제로 파일 삭제
            customFileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");
    }
    
    // 상품 삭제
    @DeleteMapping("{pno}")
    public Map<String, String> remove(@PathVariable("pno")Long pno) { // json 형태로 사용하기때문에 Map<String, String> 사용
        // 삭제할 파일 알아내기
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        customFileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "DELETE SUCCESS");
    }

    //

}
