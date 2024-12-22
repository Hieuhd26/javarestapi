package BackendService.dto.request;

import BackendService.common.Gender;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@ToString
public class UserUpdateRequest implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date dob;
    private String username;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
