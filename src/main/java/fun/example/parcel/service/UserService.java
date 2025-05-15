package fun.example.parcel.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fun.example.parcel.db.dto.UserDto;
import fun.example.parcel.db.entity.UserEntity;
import fun.example.parcel.db.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service()
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // Logger logger = LoggerFactory.getLogger(UserService.class);

    final UserRepository userRepository;

    @Data
    public static class SignupRequest {
        @NotBlank(message = "Name is required")
        final String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        final String email;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class SignupResponse {
        private Boolean error;
        private String message;
    }

    public ResponseEntity<SignupResponse> signup(SignupRequest request) {
        try {
            //fetch user from email and check unique email or not
            UserEntity user = new UserEntity();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SignupResponse(false, "User created successfully"));
        } catch (IllegalArgumentException err) {
            log.error("{}", err);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(true, "Email used"));
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserDto::fromUserEntity).toList();
    }

}
