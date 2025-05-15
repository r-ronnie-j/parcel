package fun.example.parcel.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fun.example.parcel.db.dto.LocationDto;
import fun.example.parcel.db.dto.ProductDto;
import fun.example.parcel.db.entity.DeliveryStatus;
import fun.example.parcel.db.entity.LocationEntity;
import fun.example.parcel.db.entity.ProductEntity;
import fun.example.parcel.db.entity.ProductStatusEntity;
import fun.example.parcel.db.entity.TransitEntity;
import fun.example.parcel.db.repository.ProductRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Data
    @RequiredArgsConstructor
    @Builder()
    public static class AddProductRequest {
        private final String name;
        private final String description;
        private final Double price;
        private final Long userId;
        private final List<Long> locationIds;
    }

    @Data
    @RequiredArgsConstructor
    @Builder()
    public static class AddProductResponse {
        private final Boolean error;
        private final String message;
    }

    public List<ProductDto> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream().map(ProductDto::fromProduct).toList();
    }

    public ProductDto getProductById(Long id) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        return ProductDto.fromProduct(product);
    }


    

    public AddProductResponse addProduct(AddProductRequest product) {
        try {

            ProductEntity p = new ProductEntity(product.name, product.description, product.price, product.userId);

            // Create transit for different locations of parcel
            for (Long locationId : product.locationIds) {
                TransitEntity transit = TransitEntity.builder()
                        .product(p)
                        .location(LocationEntity.builder().id(locationId).build())
                        .productStatus(new ArrayList<>())
                        .build();
                p.getTransits().add(transit);
            }

            // if transits are not empty update the status for first transit as
            // Out_For_Delivery
            if (!p.getTransits().isEmpty()) {
                TransitEntity firstTransit = p.getTransits().get(0);

                ProductStatusEntity productStatus = ProductStatusEntity.builder()
                        .transit(firstTransit)
                        .status(DeliveryStatus.Out_For_Delivery)
                        .build();

                firstTransit.getProductStatus().add(productStatus);
            }

            ProductEntity savedProduct = productRepository.save(p);

            logger.warn("The product has been added with id: {}", savedProduct.getId());

            return AddProductResponse.builder()
                    .error(false)
                    .message("Product added")
                    .build();

        } catch (Exception err) {
            logger.error("Failed to add product", err);
            throw err;
        }
    }

    public LocationDto getCurrentLocation(Long id) {
        return LocationDto.fromLocation(productRepository.findCurrentLocationOfProduct(id));
    }

}