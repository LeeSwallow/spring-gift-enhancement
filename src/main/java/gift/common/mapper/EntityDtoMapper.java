package gift.common.mapper;

import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductDefaultResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.dto.user.UserAdminResponse;
import gift.dto.user.UserCreateRequest;
import gift.dto.user.UserDefaultResponse;
import gift.dto.user.UserUpdateRequest;
import gift.dto.wishlist.WishedProductResponse;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.UserRole;
import gift.entity.WishedProduct;

import java.util.stream.Collectors;

public class EntityDtoMapper {

    private EntityDtoMapper() {
        // 인스턴스 생성 방지
    }

    public static Product toEntity(ProductCreateRequest request) {
        return new Product(
            null,
            request.name(),
            request.price(),
            request.imageUrl(),
            null
        );
    }

    public static Product toEntity(ProductUpdateRequest request) {
        return new Product(
            null, // ID는 나중에 설정될 것으로 가정
            request.name(),
            request.price(),
            request.imageUrl(),
            null
        );
    }

    public static User toEntity(UserCreateRequest request) {
        var mappedRoles = request.roles().stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
        var user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRoles(mappedRoles);
        return user;
    }

    public static User toEntity(UserUpdateRequest request) {
        var user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password());
        if (request.roles() != null) {
            var mappedRoles = request.roles().stream()
                    .map(UserRole::valueOf)
                    .collect(Collectors.toSet());
            user.setRoles(mappedRoles);
        }
        return user;
    }

    public static ProductDefaultResponse toDto(Product product) {
        return new ProductDefaultResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }

    public static WishedProductResponse toDto(WishedProduct wishedProduct) {
        Product product = wishedProduct.getProduct();
        Long subtotal = product.getPrice() * wishedProduct.getQuantity();

        return new WishedProductResponse(
                wishedProduct.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                wishedProduct.getQuantity(),
                subtotal,
                wishedProduct.getCreatedAt(),
                wishedProduct.getUpdatedAt()
        );
    }

    public static UserDefaultResponse toDto(User user) {
        return new UserDefaultResponse(
            user.getId(),
            user.getEmail()
        );
    }

    public static UserAdminResponse toAdminDto(User user) {
        return new UserAdminResponse(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getRoles().stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList()),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
