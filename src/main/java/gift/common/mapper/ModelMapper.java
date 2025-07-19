package gift.common.mapper;

import gift.common.model.CustomPage;
import gift.common.model.CustomOrder;
import gift.common.model.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    private ModelMapper() {
        // 객체 생성을 방지하기 위한 private 생성자
    }

    private static List<CustomOrder> extractCustomOrders(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return null;
        }
        List<CustomOrder> customOrders = new ArrayList<>();
        for (Sort.Order order : sort) {
            customOrders.add(toCustomOrder(order));
        }
        return customOrders;
    }

    public static CustomOrder toCustomOrder(Sort.Order order) {
        SortDirection direction = order.isAscending()
                ? SortDirection.ASC : SortDirection.DESC;

        return new CustomOrder(
                order.getProperty(),
                direction
        );
    }

    public static <T> CustomPage<T> toCustomPage(Page<T> page) {
        return new CustomPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                extractCustomOrders(page.getSort()),
                null
        );
    }
}
