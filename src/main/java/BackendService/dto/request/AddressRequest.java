package BackendService.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
@Builder
@Setter
@Getter
public class AddressRequest implements Serializable {
    private String apartmentNumber;
    private String floor;
    private String building;
    private String street;
    private String city;
    private String country;
    private Integer addressType;
}
