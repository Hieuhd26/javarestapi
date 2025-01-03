package BackendService.service;

import BackendService.dto.request.PasswordRequest;
import BackendService.dto.request.UserCreationRequest;
import BackendService.dto.request.UserUpdateRequest;
import BackendService.dto.response.PageUserResponse;
import BackendService.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    int saveUser(UserCreationRequest request);

    void updateUser(UserUpdateRequest request);

    void changePassword(PasswordRequest request);

    void deleteUser(Integer userId);

    UserResponse getUserDeTail(Integer userId);

   PageUserResponse getAllUser(String keyword, String sort, int size, int page);
}
