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

    @GetMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<WishedProductResponse> getWishlistItem(
            @PathVariable Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        WishedProduct wishedProduct = wishedProductService.getById(auth.userId(), id);
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<WishedProductResponse> addWishlistItem(
            @RequestBody CreateWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        WishedProduct wishedProduct = wishedProductService.create(auth.userId(), request.productId(), request.quantity());
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<?> updateWishlistItem(
            @PathVariable Long id,
            @RequestBody UpdateWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        var wishedProduct = wishedProductService.update(auth.userId(), id, request.quantity());
        if (wishedProduct.isEmpty()) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct.get()), HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<?> patchWishlistItem(
            @PathVariable Long id,
            @RequestBody PatchWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        Optional<WishedProduct> wishedProduct;
        if (request.increment()) {
            wishedProduct = wishedProductService.increaseProductQuantity(auth.userId(), id, request.quantity());
        } else {
            wishedProduct = wishedProductService.decreaseProductQuantity(auth.userId(), id, request.quantity());
        }
        if (wishedProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(WishedProductResponse.from(wishedProduct.get()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteWishlistItem(
            @PathVariable Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        wishedProductService.delete(auth.userId(), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteAllWishlistItems(
            @RequestAttribute("auth") CustomAuth auth
    ) {
        wishedProductService.deleteAll(auth.userId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
