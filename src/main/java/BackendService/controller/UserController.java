package BackendService.controller;

import BackendService.dto.request.PasswordRequest;
import BackendService.dto.request.UserCreationRequest;
import BackendService.dto.request.UserUpdateRequest;
import BackendService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@Slf4j(topic = "USER-CONTROLLER")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create User", description = "API add new user to database")
    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestBody UserCreationRequest request) {
        log.info("Create User: {}", request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "User created successfully");
        result.put("data", userService.saveUser(request));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update User", description = "API update new user to database")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody UserUpdateRequest request) {
        log.info("Update User: {}", request);
        userService.updateUser(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "User updated successfully");
        result.put("data", "");
        return result;
    }

    @Operation(summary = "Change password", description = "API change password")
    @PatchMapping("/changepw")
    public Map<String, Object> changePassword(@RequestBody PasswordRequest request) {
        log.info("Change Password: {}", request);
        userService.changePassword(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Change password successfully");
        result.put("data", "");
        return result;
    }

    @Operation(summary = "Delete user", description = "API delete user from database")
    @DeleteMapping("/del/{userId}")
    public Map<String, Object> changePassword(@PathVariable Integer userId) {
        log.info("Delete user : {}", userId);
        userService.deleteUser(userId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "Deleted user successfully");
        result.put("data", "");
        return result;
    }

    @Operation(summary = "Get user by Id", description = "API get user by id from database")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable Integer userId) {
        log.info("Get userDetail : {}", userId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Get userDetail successfully");
        result.put("data", userService.getUserDeTail(userId));
        return result;
    }

    @Operation(summary = "Get user list", description = "API retrieve user from database")
    @GetMapping()
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        log.info("Get user list");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user list");
        result.put("data", userService.getAllUser(keyword, sort, page, size));

        return result;
    }


}
