package BackendService.dto.response;

import java.io.Serializable;
import java.util.Date;

public class UserResponse implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phone;
    private Date dob;
}
