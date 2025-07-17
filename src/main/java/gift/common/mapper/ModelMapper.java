package gift.common.mapper;

import gift.common.model.CustomPage;
import gift.common.model.CustomOrder;
import gift.common.model.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModelMapper {

    private ModelMapper() {
        // 객체 생성을 방지하기 위한 private 생성자
    }
    public static <T> CustomPage<T> toCustomPage(Page<T> page) {
        return new CustomPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                null,
                null
        );
    }

    public static <T> CustomPage<T> toCustomPage(Page<T> page, List<CustomOrder> sort) {
        return new CustomPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                sort,
                null
        );
    }

    public static List<CustomOrder> toCustomOrders(List<String> sortParams) {
        if (sortParams == null || sortParams.isEmpty()) {
            throw new IllegalArgumentException("정렬 파라미터는 필수입니다.");
        }
        List<CustomOrder> orders = new ArrayList<>();
        Optional<String> prevField = Optional.empty();

        for (String param : sortParams) {
            if (SortDirection.contains(param)) {
                // 정렬 방향이 지정된 경우
                if (prevField.isEmpty()) {
                    throw new IllegalArgumentException("정렬 필드가 지정되지 않았습니다.");
                }
                String field = prevField.get();
                SortDirection direction = SortDirection.valueOf(param.toUpperCase());
                orders.add(new CustomOrder(field, direction));
                prevField = Optional.empty();
            } else if (prevField.isPresent()) {
                // 이전 필드가 있고 현재 파라미터가 필드인 경우
                orders.add(new CustomOrder(prevField.get(), SortDirection.ASC)); // 기본 정렬 방향은 ASC로 설정
                prevField = Optional.empty();
            } else {
                // 현재 파라미터가 필드인 경우
                prevField = Optional.of(param);
            }
        }
        // 마지막에 남아있는 필드가 있다면 기본 정렬 방향으로 추가
        prevField.ifPresent(s -> orders.add(new CustomOrder(s, SortDirection.ASC)));
        return orders;
    }

    public static Sort.Order toOrder(CustomOrder customOrder) {
        return switch (customOrder.direction()) {
            case ASC -> Sort.Order.asc(customOrder.field());
            case DESC -> Sort.Order.desc(customOrder.field());
        };
    }

    public static Sort toSort(List<CustomOrder> customOrders) {
        if (customOrders == null || customOrders.isEmpty()) {
            return Sort.unsorted();
        }
        return Sort.by(customOrders.stream()
                .map(ModelMapper::toOrder)
                .toList());
    }

}
