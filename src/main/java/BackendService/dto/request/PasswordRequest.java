package BackendService.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class PasswordRequest implements Serializable {
    private int id;
    private String password;
    private String confirmPassword;
}
