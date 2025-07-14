package gift.dto.product;

import gift.entity.Product;
import gift.common.validation.annotation.KakaoNotContained;
import gift.common.validation.annotation.ValidProductName;
import gift.common.validation.group.AuthenticationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record ProductUpdateRequest (
    @NotBlank(message = "상품명은 빈칸일 수 없습니다.")
    @ValidProductName
    @KakaoNotContained(groups = {AuthenticationGroups.UserGroup.class})
    String name,
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    Long price,
    String imageUrl
) {
    public ProductUpdateRequest(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toEntity(Long productId) {
        return new Product(productId, name, price, imageUrl, null);
    }
}
