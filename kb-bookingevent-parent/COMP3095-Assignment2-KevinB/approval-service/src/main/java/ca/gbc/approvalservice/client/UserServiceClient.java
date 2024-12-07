package ca.gbc.approvalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}/roles")
    List<String> getUserRoles(@PathVariable("userId") Long userId);
}
