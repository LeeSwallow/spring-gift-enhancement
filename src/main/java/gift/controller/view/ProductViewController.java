package gift.controller.view;

import gift.common.aop.annotation.PreAuthorize;
import gift.common.model.CustomAuth;
import gift.common.model.CustomPage;
import gift.dto.product.ProductCreateRequest;
import gift.entity.Product;
import gift.entity.UserRole;
import gift.service.product.ProductService;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {
    private final ProductService productService;
    private final Validator validator;

    public ProductViewController(
            ProductService productService,
            Validator validator
    ) {
        this.productService = productService;
        this.validator = validator;
    }

    private void validateRequest(ProductCreateRequest request) {
        var violations = validator.validate(request)
                .stream()
                .findFirst();
        if (violations.isPresent()) {
            throw new IllegalArgumentException(violations.get().getMessage());
        }
    }

    @PreAuthorize(UserRole.ROLE_ADMIN)
    @GetMapping
    public String showProductList(
        Model model,
        @RequestParam(value = "page", defaultValue = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
        @RequestParam(value = "size", defaultValue = "5")
        @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
    ) {
        CustomPage<Product> currentPage = productService.getBy(page, size);
        int start = Math.max(0, currentPage.getPage() - 2);
        int end = Math.min(currentPage.getTotalPages() - 1, currentPage.getPage() + 2);

        model.addAttribute("title", "관리자 상품 목록");
        model.addAttribute("pageInfo", currentPage);
        model.addAttribute("pageStart", start);
        model.addAttribute("pageEnd", end);

        return "admin/product/product-list";
    }

    @PreAuthorize(UserRole.ROLE_ADMIN)
    @GetMapping("/{id}")
    public String showProductDetails(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            Model model
    ) {
        Product product = productService.getById(id);

        model.addAttribute("title", "상품 상세 정보");
        model.addAttribute("product", product);
        return "admin/product/product-detail";
    }

    @PreAuthorize(UserRole.ROLE_ADMIN)
    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("title", "상품 등록");
        return "admin/product/create-product";
    }

    @PreAuthorize(UserRole.ROLE_ADMIN)
    @PostMapping("/create")
    public String createProduct(
            @ModelAttribute ProductCreateRequest request,
            @RequestAttribute("auth") CustomAuth auth,
            Model model
    ) {
        try {
            validateRequest(request);
            Product product = request.toProduct();
            Product createdProduct = productService.create(product, auth);
            return "redirect:/admin/products/" + createdProduct.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/product/create-product";
        }
    }

}
