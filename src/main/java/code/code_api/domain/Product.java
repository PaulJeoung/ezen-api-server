package code.code_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }


    public void addImage(ProductImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String filename) {
        ProductImage productImage = ProductImage.builder()
                .fileName(filename)
                .build();
        addImage(productImage);
    }

    public void clearList() {
        this.imageList.clear();
    }

}
