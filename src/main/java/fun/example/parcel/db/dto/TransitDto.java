package fun.example.parcel.db.dto;

import java.util.List;

import fun.example.parcel.db.entity.TransitEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class TransitDto {
    private final Long id;
    private final LocationDto location;
    private final List<ProductStatusDto> productStatus;

    public static TransitDto fromTransitEntity(TransitEntity entity) {
        return TransitDto.builder()
                .id(entity.getId())
                .location(LocationDto.fromLocation(entity.getLocation()))
                .productStatus(entity.getProductStatus().stream()
                        .map(a -> ProductStatusDto.fromProductStatus(a))
                        .toList())
                .build();
    }
}
