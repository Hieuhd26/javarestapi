package BackendService.service.impl;

import BackendService.common.UserStatus;
import BackendService.dto.request.PasswordRequest;
import BackendService.dto.request.UserCreationRequest;
import BackendService.dto.request.UserUpdateRequest;
import BackendService.dto.response.PageUserResponse;
import BackendService.dto.response.UserResponse;
import BackendService.exception.AppException;
import BackendService.model.AddressEntity;
import BackendService.model.UserEntity;
import BackendService.repository.AddressRepository;
import BackendService.repository.UserRepository;
import BackendService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public void changePassword(PasswordRequest request) {
        log.info("Changing password for user: {}", request);
        UserEntity user = findUserById(request.getId());
        if (request.getPassword().equals(request.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);
        log.info("change password for user: {}", user);
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("Deleting user: {}", userId);
        UserEntity user = findUserById(userId);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("Deleted user: {}", userId);
    }

    @Override
    public UserResponse getUserDeTail(Integer userId) {
        log.info("Getting user detail by id");

        UserEntity user = findUserById(userId);

        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDob())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
        return userResponse;
    }

    public PageUserResponse getAllUser(String keyword, String sort, int page, int size) {
        log.info("Get all use ");

        //Sort
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }
        //Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));
        Page<UserEntity> entityPage;

        if (StringUtils.hasLength(keyword)) {
            keyword = "%" + keyword.toLowerCase() + "%";
            entityPage = userRepository.searchByKeyWord(keyword, pageable);
        } else {
            entityPage = userRepository.findAll(pageable);
        }

        PageUserResponse response = getPageUserResponse(page, size, entityPage);
        return response;
    }

    private static PageUserResponse getPageUserResponse(int page, int size, Page<UserEntity> userEntities) {
        //convert from UserEntity to UserResponse
        log.info("Convert userEntity");
        List<UserResponse> userResponses = userEntities.stream().map(entity -> {
            return UserResponse.builder()
                    .id(entity.getId())
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .gender(entity.getGender())
                    .dob(entity.getDob())
                    .phone(entity.getPhone())
                    .email(entity.getEmail())
                    .username(entity.getUsername())
                    .build();
        }).toList();
        PageUserResponse response = new PageUserResponse();
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalPage(userEntities.getTotalPages());
        response.setTotalElement(userEntities.getTotalElements());
        response.setUserResponseList(userResponses);
        return response;
    }
}