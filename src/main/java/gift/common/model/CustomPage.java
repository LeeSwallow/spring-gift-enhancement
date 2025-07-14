package gift.common.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPage<T> {
    private final List<T> contents;
    private final Integer page;
    private final Integer size;
    private final Integer totalElements;
    private final Integer totalPages;
    private final Map<String, Object> extras;

    private CustomPage(List<T> contents, Integer page, Integer size, Integer totalElements, Integer totalPages, Map<String, Object> extras) {
        this.contents = contents;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.extras = extras;
    }

    public List<T> getContents() {
        return contents;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    @JsonAnyGetter
    public Map<String, Object> getExtras() {
        return extras;
    }

    public static class Builder<T> {
        private final List<T> contents;
        private final Integer totalElements;
        private Integer page = 0;
        private Integer size = 5;
        private Map<String, Object> extras = null;

        public Builder(List<T> contents, Integer totalElements) {
            if (contents == null || totalElements == null) {
                throw new IllegalArgumentException("contents와 totalElements는 null일 수 없습니다!");
            }
            this.contents = contents;
            this.totalElements = totalElements;
        }

        public Builder<T> page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder<T> size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder<T> extra(String key, Object value) {
            if (this.extras == null) {
                this.extras = new java.util.HashMap<>();
            }
            this.extras.put(key, value);
            return this;
        }

        public CustomPage<T> build() {
            if (this.page < 0 || this.size <= 0) {
                throw new IllegalArgumentException("페이지 번호는 0 이상이어야 하고, 크기는 1 이상이어야 합니다!");
            }
            int totalPages = (int) Math.ceil((double) totalElements / size);

            if (!(totalPages == 0 && page == 0) && this.page >= totalPages) {
                throw new IllegalArgumentException("페이지 번호가 총 페이지 수를 초과했습니다!");
            }

            return new CustomPage<>(
                    this.contents,
                    this.page,
                    this.size,
                    this.totalElements,
                    totalPages,
                    this.extras
            );
        }
    }

    public static <T> Builder<T> builder(List<T> contents, Integer totalElements) {
        return new Builder<>(contents, totalElements);
    }

    public static <F, T> CustomPage<T> convert(CustomPage<F> page, Function<F, T> converter) {
        return new CustomPage<>(
                page.getContents().stream().map(converter).toList(),
                page.getPage(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getExtras()

        );
    }
}
