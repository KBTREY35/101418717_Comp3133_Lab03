package ca.gbc.userservice.controller;


import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/user/" + createdUser.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdUser);
    }

    // READ
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET BY ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable("userId") Long userId,
                                           @RequestBody UserRequest userRequest) {
        Long updatedUserId = userService.updateUser(userId, userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/user/" + updatedUserId);

        return ResponseEntity.noContent().headers(headers).build();
    }

    // DELETE
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
