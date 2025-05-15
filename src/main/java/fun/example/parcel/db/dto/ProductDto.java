package fun.example.parcel.db.dto;

import java.time.LocalDateTime;
import java.util.List;

import fun.example.parcel.db.entity.ProductEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ProductDto {

    private final Long id;
    private final String name;
    private final String description;
    private final Double price;
    private final LocalDateTime createdAt;
    private final List<TransitDto> transits;

    public static ProductDto fromProduct(ProductEntity product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .price(product.getPrice())
                .transits(product.getTransits().stream()
                        .map(TransitDto::fromTransitEntity)
                        .toList())
                .build();
    }
}
