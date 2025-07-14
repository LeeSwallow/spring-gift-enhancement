package gift.controller.api;

import gift.common.aop.annotation.PreAuthorize;
import gift.common.model.CustomAuth;
import gift.common.model.CustomPage;
import gift.dto.wishlist.CreateWishedProductRequest;
import gift.dto.wishlist.PatchWishedProductRequest;
import gift.dto.wishlist.UpdateWishedProductRequest;
import gift.dto.wishlist.WishedProductResponse;
import gift.entity.UserRole;
import gift.entity.WishedProduct;
import gift.service.wishlist.WishedProductService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {
    private final WishedProductService wishedProductService;

    public WishlistController(WishedProductService wishedProductService) {
        this.wishedProductService = wishedProductService;
    }

    @GetMapping
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<CustomPage<WishedProductResponse>> getWishlist(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        var pagedResponse = wishedProductService.getAll(auth.userId(), page, size);

        return new ResponseEntity<>(
                CustomPage.convert(pagedResponse, WishedProductResponse::from), HttpStatus.OK
        );
    }

    @GetMapping("/{productId}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<WishedProductResponse> getWishlistItem(
            @PathVariable Long productId,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        WishedProduct wishedProduct = wishedProductService.getByProductId(auth.userId(), productId);
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<WishedProductResponse> addWishlistItem(
            @RequestBody CreateWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        WishedProduct wishedProduct = wishedProductService.addProduct(auth.userId(), request.productId(), request.quantity());
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<?> updateWishlistItem(
            @PathVariable Long productId,
            @RequestBody UpdateWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        var wishedProduct = wishedProductService.updateProduct(auth.userId(), productId, request.quantity());
        if (wishedProduct.isEmpty()) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct.get()), HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<?> patchWishlistItem(
            @PathVariable Long productId,
            @RequestBody PatchWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        Optional<WishedProduct> wishedProduct;
        if (request.increment()) {
            wishedProduct = wishedProductService.increaseProductQuantity(auth.userId(), productId, request.quantity());
        } else {
            wishedProduct = wishedProductService.decreaseProductQuantity(auth.userId(), productId, request.quantity());
        }
        if (wishedProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct.get()), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteWishlistItem(
            @PathVariable Long productId,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        wishedProductService.removeProduct(auth.userId(), productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteAllWishlistItems(
            @RequestAttribute("auth") CustomAuth auth
    ) {
        wishedProductService.removeAllProducts(auth.userId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
