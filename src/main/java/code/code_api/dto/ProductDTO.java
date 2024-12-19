package code.code_api.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ProductDTO {

    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    private List<MultipartFile> files = new ArrayList<>();

    private List<String> uploadFileNames = new ArrayList<>();

}
