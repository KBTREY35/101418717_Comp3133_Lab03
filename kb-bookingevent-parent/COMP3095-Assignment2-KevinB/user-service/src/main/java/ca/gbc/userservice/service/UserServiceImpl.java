package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.debug("Creating a new user {}", userRequest.name());

        User user = User.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .role(userRequest.role())
                .build();

        User savedUser = userRepository.save(user);

        log.info("User {} is saved", savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.debug("Getting all users");

        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        log.debug("Getting user with ID {}", userId);

        return userRepository.findById(userId)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Long updateUser(Long userId, UserRequest userRequest) {
        log.debug("Updating user {}", userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setRole(userRequest.role());
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("Deleting user {}", userId);
        userRepository.deleteById(userId);
    }

}
