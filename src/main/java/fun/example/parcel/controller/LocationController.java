package fun.example.parcel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fun.example.parcel.db.dto.LocationDto;
import fun.example.parcel.db.dto.ProductDto;
import fun.example.parcel.service.LocationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @GetMapping("/{locationId}")
    public ResponseEntity<CustomResponse<LocationDto>> getLocationFromId(@PathVariable Long locationId) {
        try {
            var location = locationService.getLocationById(locationId);
            if (location == null) {
                return ResponseEntity.badRequest().body(new CustomResponse<>(false, "Location not found", null));
            }
            return ResponseEntity.ok(new CustomResponse<>(true, "Location found", location));
        } catch (Exception err) {
            logger.error("Error fetching location: {}", err);
            return ResponseEntity.internalServerError()
                    .body(new CustomResponse<>(false, "Internal server error", null));
        }
    }

    @GetMapping("/allLocation")
    public ResponseEntity<CustomResponse<List<LocationDto>>> getAllLocation() {
        try {
            var locations = locationService.getAllLocations();
            if (locations.isEmpty()) {
                return ResponseEntity.badRequest().body(new CustomResponse<>(false, "No locations found", null));
            }
            return ResponseEntity.ok(new CustomResponse<>(true, "Locations found", locations));
        } catch (Exception err) {
            logger.error("Error fetching locations: {}", err);
            return ResponseEntity.internalServerError()
                    .body(new CustomResponse<>(false, "Internal server error", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CustomResponse<LocationDto>> addLocation(
            @RequestBody LocationService.AddLocationRequest request) {
        try {
            LocationDto location = locationService.addLocation(request);
            return ResponseEntity.ok(new CustomResponse<>(true, "Location added", location));
        } catch (DataIntegrityViolationException ex) {
            logger.error("Data integrity violation while adding location: {}", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(new CustomResponse<>(
                            false,
                            "The given email " + request.getEmail() + " is alredy taken.",
                            null));
        } catch (Exception ex) {
            logger.error("Error adding location: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError()
                    .body(new CustomResponse<>(false, "Internal server error", null));
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<CustomResponse<List<ProductDto>>> getProductsAtLocationFromLocationId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(new CustomResponse<>(false, "Got product at the given location",
                    locationService.getProductAtGivenLocation(id)));
        } catch (Exception e) {
            logger.error("Failed to get product form given location {}", e);
            return ResponseEntity.badRequest().body(
                    new CustomResponse<>(true, "Failed to get the product at given location", null));
        }
    }

}
