package BackendService.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageUserResponse extends PageResponse implements Serializable {
    private List<UserResponse> userResponseList;
}
