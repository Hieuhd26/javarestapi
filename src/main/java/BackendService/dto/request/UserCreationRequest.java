package BackendService.dto.request;

import BackendService.common.Gender;
import BackendService.common.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Builder
@Getter
@Setter
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String username;
    private String phone;
    private Date dob;
    private UserType type;
    private List<AddressRequest> addresses;
}
