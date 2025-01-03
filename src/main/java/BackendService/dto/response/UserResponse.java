package BackendService.dto.response;

import BackendService.common.Gender;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class UserResponse implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String phone;
    private Date dob;
    private String username;
}
