package gift.controller.api;

import gift.common.aop.annotation.PreAuthorize;
import gift.common.mapper.EntityDtoMapper;
import gift.common.model.CustomAuth;
import gift.common.model.CustomPage;
import gift.common.validation.annotation.AllowedSortFields;
import gift.dto.wishlist.CreateWishedProductRequest;
import gift.dto.wishlist.PatchWishedProductRequest;
import gift.dto.wishlist.UpdateWishedProductRequest;
import gift.dto.wishlist.WishedProductResponse;
import gift.entity.UserRole;
import gift.entity.WishedProduct;
import gift.service.wishlist.WishedProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
            @AllowedSortFields(
                value = {"id", "product.price", "product.name", "quantity", "createdAt", "updatedAt"},
                showAllowedFields = true
            )
            @PageableDefault(size = 5) Pageable pageable,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        CustomPage<WishedProduct> wishlistPage = wishedProductService.findAllBy(auth.userId(), pageable);
        return new ResponseEntity<>(
                CustomPage.convert(wishlistPage, EntityDtoMapper::toDto), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<WishedProductResponse> getWishlistItem(
            @PathVariable Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        WishedProduct wishedProduct = wishedProductService.findBy(auth.userId(), id);
        return new ResponseEntity<>(EntityDtoMapper.toDto(wishedProduct), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<WishedProductResponse> addWishlistItem(
            @Valid @RequestBody CreateWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        WishedProduct wishedProduct = wishedProductService.create(auth.userId(), request.productId(), request.quantity());
        return new ResponseEntity<>(EntityDtoMapper.toDto(wishedProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<?> updateWishlistItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        var wishedProduct = wishedProductService.updateQuantityBy(auth.userId(), id, request.quantity());
        if (wishedProduct.isEmpty()) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(EntityDtoMapper.toDto(wishedProduct.get()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<?> patchWishlistItem(
            @PathVariable Long id,
            @Valid @RequestBody PatchWishedProductRequest request,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        Optional<WishedProduct> wishedProduct;
        if (request.increment()) {
            wishedProduct = wishedProductService.increaseQuantityBy(auth.userId(), id, request.quantity());
        } else {
            wishedProduct = wishedProductService.decreaseQuantityBy(auth.userId(), id, request.quantity());
        }
        if (wishedProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(EntityDtoMapper.toDto(wishedProduct.get()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteWishlistItem(
            @PathVariable Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        wishedProductService.deleteBy(auth.userId(), id);
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
