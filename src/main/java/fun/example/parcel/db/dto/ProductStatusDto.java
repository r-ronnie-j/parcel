package fun.example.parcel.db.dto;

import java.time.LocalDateTime;

import fun.example.parcel.db.entity.DeliveryStatus;
import fun.example.parcel.db.entity.ProductStatusEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class ProductStatusDto {
    private final Long id;
    private final LocalDateTime createdAt;
    private final DeliveryStatus status;

    public static ProductStatusDto fromProductStatus(ProductStatusEntity entity) {
        return ProductStatusDto
                .builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .build();
    }
}
