package BackendService.service.impl;

import BackendService.common.UserStatus;
import BackendService.dto.request.UserCreationRequest;
import BackendService.dto.request.UserUpdateRequest;
import BackendService.exception.AppException;
import BackendService.model.AddressEntity;
import BackendService.model.UserEntity;
import BackendService.repository.AddressRepository;
import BackendService.repository.UserRepository;
import BackendService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveUser(UserCreationRequest request) {
        log.info("Saving user: {}", request);
        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dob(request.getDob())
                .username(request.getUsername())
                .type(request.getType())
                .status(UserStatus.NONE)
                .build();
        userRepository.save(user);
        log.info("Save user: {}", user);
        if (user.getId() != null) { //null chi cho Interger chu khong cho int
            log.info("User id: {}", user.getId());
            List<AddressEntity> addressEntityList = new ArrayList<>();
            request.getAddresses().forEach(addressRequest -> {
                AddressEntity address = AddressEntity.builder()
                        .apartmentNumber(addressRequest.getApartmentNumber())
                        .floor(addressRequest.getFloor())
                        .building(addressRequest.getBuilding())
                        .street(addressRequest.getStreet())
                        .city(addressRequest.getCity())
                        .country(addressRequest.getCountry())
                        .addressType(addressRequest.getAddressType())
                        .userId(user.getId())
                        .build();
                addressEntityList.add(address);
            });
            addressRepository.saveAll(addressEntityList);
            log.info("Saved address: {}", addressEntityList);
        }
        return user.getId();
    }

    private UserEntity findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException("User not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateRequest request) {
        log.info("Updating user: {}", request);
        UserEntity user = findUserById(request.getId());

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);
        log.info("Updated user: {}", user);

        List<AddressEntity> list = new ArrayList<>();

        request.getAddresses().forEach(address -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(user.getId(), address.getAddressType());
            if (addressEntity != null) {
                addressEntity = new AddressEntity();
            }
            addressEntity.setApartmentNumber(address.getApartmentNumber());
            addressEntity.setFloor(address.getFloor());
            addressEntity.setBuilding(address.getBuilding());
            addressEntity.setStreet(address.getStreet());
            addressEntity.setCity(address.getCity());
            addressEntity.setCountry(address.getCountry());
            addressEntity.setAddressType(address.getAddressType());
            addressEntity.setUserId(user.getId());
            list.add(addressEntity);
        });

        addressRepository.saveAll(list);
        log.info("Updated address: {}", list);

    }
}
