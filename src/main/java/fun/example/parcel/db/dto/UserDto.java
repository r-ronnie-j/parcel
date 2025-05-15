package fun.example.parcel.db.dto;

import java.time.LocalDateTime;

import fun.example.parcel.db.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserDto {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;

    public static UserDto fromUserEntity(UserEntity entity) {
        return UserDto.builder().createdAt(entity.getCreatedAt()).id(entity.getId()).email(entity.getEmail())
                .name(entity.getName()).build();
    }
}
