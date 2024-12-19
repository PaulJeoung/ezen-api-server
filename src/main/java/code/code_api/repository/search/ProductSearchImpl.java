package code.code_api.repository.search;

import code.code_api.domain.Product;
import code.code_api.domain.QProduct;
import code.code_api.domain.QProductImage;
import code.code_api.dto.PageRequestDTO;
import code.code_api.dto.PageResponseDTO;
import code.code_api.dto.ProductDTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl(){
        super((Product.class));
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {
        log.info("ProductSearchImpl() ? 동작하고 있어?");
        Pageable pageable =  PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        // product 기준으로 leftjoin
        query.leftJoin(product.imageList, productImage);
        query.where(productImage.ord.eq(0));

        // 페이징 처리 된 쿼리
        getQuerydsl().applyPagination(pageable, query);
        List<Product> productList1 = query.fetch();

        // 상품과 상품 이미지를 뽑고 싶을때
        List<Tuple> productList2 = query.select(product, productImage).fetch();
        long count = query.fetchCount();

        // log.info("상품리스트 1 {}", productList1);
        log.info("상품리스트 2 {}", productList2);

        return null;
    }


}
