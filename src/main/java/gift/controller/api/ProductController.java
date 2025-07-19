package gift.controller.api;

import gift.common.aop.annotation.PreAuthorize;
import gift.common.mapper.EntityDtoMapper;
import gift.common.model.CustomAuth;
import gift.common.validation.annotation.AllowedSortFields;
import gift.dto.product.ProductDefaultResponse;
import gift.common.model.CustomPage;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductUpdateRequest;
import gift.entity.Product;
import gift.entity.UserRole;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<CustomPage<ProductDefaultResponse>> getAllProducts(
            @AllowedSortFields(value = { "id", "name", "price", "createdAt", "updatedAt" }, showAllowedFields = true)
            @PageableDefault(size = 5) Pageable  pageable
    ) {
        CustomPage<Product> productPage = productService.findAllBy(pageable);
        return new ResponseEntity<>(CustomPage.convert(
                productPage, EntityDtoMapper::toDto), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDefaultResponse> getProductById(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id
    ) {
        Product product = productService.findById(id);
        return new ResponseEntity<>(EntityDtoMapper.toDto(product), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<ProductDefaultResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        Product product = productService.create(EntityDtoMapper.toEntity(dto), auth.role(), auth.userId());
        log.info("상품 생성 성공: {}", product);
        return new ResponseEntity<>(EntityDtoMapper.toDto(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<ProductDefaultResponse> updateProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        var product = EntityDtoMapper.toEntity(dto);
        product.setId(id);
        Product updatedProduct = productService.update(product, auth.role(), auth.userId());
        log.info("상품 업데이트 성공: {}", updatedProduct);
        return new ResponseEntity<>(EntityDtoMapper.toDto(updatedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        productService.deleteById(id, auth.role(), auth.userId());
        log.info("상품 삭제 성공: ID={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
