package fun.example.parcel.db.dto;

import java.time.LocalDate;

import fun.example.parcel.db.entity.LocationEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Builder;

@Getter
@RequiredArgsConstructor
@Builder
public class LocationDto {

    private final Long id;
    private final String name;
    private final String description;
    private final String email;
    private final LocalDate createdAt;

    public static LocationDto fromLocation(LocationEntity location) {
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .createdAt(location.getCreatedAt())
                .description(location.getDescription())
                .email(location.getEmail())
                .build();
    }
}
