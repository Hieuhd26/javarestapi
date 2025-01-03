package BackendService.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public abstract class PageResponse {
    private int pageSize;
    private int pageNumber;
    private long totalPage;
    private long totalElement;
}
