package gift.dto.product;

import gift.entity.Product;
import gift.common.validation.annotation.KakaoNotContained;
import gift.common.validation.annotation.ValidProductName;
import gift.common.validation.group.AuthenticationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(
    @NotNull(message = "상품명은 필수입니다.")
    @NotBlank(message = "상품명은 빈칸일 수 없습니다.")
    @ValidProductName
    @KakaoNotContained(groups = {AuthenticationGroups.UserGroup.class})
    String name,
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    Long price,
    @NotNull(message = "이미지 URL은 필수입니다.")
    String imageUrl
) {

    public ProductCreateRequest(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toProduct() {
        return new Product(null, name, price, imageUrl, null);
    }
}
