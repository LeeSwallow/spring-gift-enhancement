package gift.common.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

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
    private Map<String, Object> extras;

    private CustomPage(
            List<T> contents,
            Integer page,
            Integer size,
            Integer totalElements,
            Integer totalPages,
            Map<String, Object> extras
    ) {
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

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public static <T> CustomPage<T> from(Page<T> page) {
        return new CustomPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                null
        );
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
