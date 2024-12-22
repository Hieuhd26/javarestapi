package BackendService.controller;

import BackendService.dto.request.UserCreationRequest;
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
    public ResponseEntity<Object> updateUser(@RequestBody UserCreationRequest request) {
        log.info("Update User: {}", request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "User updated successfully");
        result.put("data", userService.saveUser(request));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }



}
