package BackendService.service;

import BackendService.dto.request.UserCreationRequest;
import BackendService.dto.request.UserUpdateRequest;

public interface UserService {
    int saveUser(UserCreationRequest request);

    void updateUser(UserUpdateRequest request);
}
