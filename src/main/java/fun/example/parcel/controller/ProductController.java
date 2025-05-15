package fun.example.parcel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fun.example.parcel.db.dto.LocationDto;
import fun.example.parcel.db.dto.ProductDto;
import fun.example.parcel.db.dto.ProductStatusDto;
import fun.example.parcel.service.ProductService;
import fun.example.parcel.service.ProductStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private final ProductStatusService productStatusService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<CustomResponse<List<ProductDto>>> getAllProducts() {
        // try {
            var products = productService.getAllProducts();
            if (products.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new CustomResponse<>(false, "No products found", null));
            }

            return ResponseEntity.ok(new CustomResponse<>(true, "Products found", products));
        // } catch (Exception err) {
        //     logger.error("Error fetching products: {}", err);
        //     return ResponseEntity.internalServerError()
        //             .body(new CustomResponse<>(false, "Internal server error", null));
        // }
    }

    @PostMapping("/addProduct")
    public ProductService.AddProductResponse addProduct(@RequestBody ProductService.AddProductRequest entity) {
        try {
            var product = productService.addProduct(entity);
            if (product.getError()) {
                return ProductService.AddProductResponse.builder().error(true).message(product.getMessage()).build();
            }
            return ProductService.AddProductResponse.builder().error(false).message("Product added successfully")
                    .build();
        } catch (Exception err) {
            logger.error("Error adding product: {}", err);
            return ProductService.AddProductResponse.builder().error(true).message("Internal server error").build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<ProductDto>> getProductById(@PathVariable String id) {
        try {
            var product = productService.getProductById(Long.parseLong(id));
            if (product == null) {
                return ResponseEntity.badRequest().body(new CustomResponse<>(false, "Product not found", null));
            }
            return ResponseEntity.ok(new CustomResponse<>(true, "Product found", product));
        } catch (Exception err) {
            logger.error("Error fetching product by id: {}", err);
            return ResponseEntity.internalServerError()
                    .body(new CustomResponse<>(false, "Internal server error", null));
        }
    }

    @GetMapping("/getProductStatus/{id}")
    public List<ProductStatusDto> getProductStatus(@PathVariable Long id) {
        try {
            var productStatus = productStatusService.getProductStatusByProductId(id);
            if (productStatus.isEmpty()) {
                return null;
            }
            return productStatus;
        } catch (Exception err) {
            logger.error("Error fetching product status: {}", err);
            return null;
        }
    }

    @PostMapping("/addStatus/{transitId}")
    public CustomResponse<ProductStatusDto> addProductStatus(@RequestBody ProductStatusDto entity,
            @PathVariable Long transitId) {
        try {
            var productStatus = productStatusService.addProductStatus(entity, transitId);
            return new CustomResponse<>(true, "Added product status", productStatus);
        } catch (Exception exception) {
            return new CustomResponse<ProductStatusDto>(false, "Failed to add the status", null);
        }
    }

    @GetMapping("/getProductLocation/{productId}")
    public ResponseEntity<CustomResponse<LocationDto>> getCurrentLocation(@PathVariable Long productId) {
        try {
            LocationDto location = productService.getCurrentLocation(productId);
            return ResponseEntity.ok()
                    .body(new CustomResponse<>(true, "Latest known location of product found", location));
        } catch (Exception e) {
            logger.error("Failed to get the current location for productId {} {}", productId, e);
            return ResponseEntity.badRequest()
                    .body(new CustomResponse<>(false, "Failed to found the location for product", null));
        }
    }

}
