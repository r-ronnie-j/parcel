package fun.example.parcel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fun.example.parcel.db.dto.ProductStatusDto;
import fun.example.parcel.db.entity.ProductStatusEntity;
import fun.example.parcel.db.entity.TransitEntity;
import fun.example.parcel.db.repository.ProductStatusRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductStatusService {

    private final ProductStatusRepository productStatusRepository;

    public List<ProductStatusDto> getProductStatusByProductId(Long productId) {
        return productStatusRepository
                .findAllByProductId(productId)
                .stream()
                .map(ProductStatusDto::fromProductStatus)
                .toList();
    }

    public ProductStatusDto addProductStatus(ProductStatusDto statusDto, Long transitId) {
        ProductStatusEntity productStatus = ProductStatusEntity.builder()
                .status(statusDto.getStatus())
                .transit(TransitEntity.builder().id(transitId).build())
                .build();
        productStatusRepository.save(productStatus);
        return ProductStatusDto.fromProductStatus(productStatus);
    }

}
