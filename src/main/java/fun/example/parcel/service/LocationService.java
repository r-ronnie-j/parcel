package fun.example.parcel.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fun.example.parcel.db.dto.LocationDto;
import fun.example.parcel.db.dto.ProductDto;
import fun.example.parcel.db.entity.LocationEntity;
import fun.example.parcel.db.repository.LocationRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Data
    @RequiredArgsConstructor
    @Builder
    public static class AddLocationRequest {
        final String name;
        final String description;
        final String email;
    }

    public LocationDto addLocation(AddLocationRequest addLocation) {
        LocationEntity li = locationRepository.save(
                LocationEntity.builder()
                        .name(addLocation.getName())
                        .description(addLocation.getDescription())
                        .email(addLocation.getEmail())
                        .build());
        logger.info("Adding location: {}", li.getId());
        return LocationDto.fromLocation(li);
    }

    public List<LocationDto> getAllLocations() {
        logger.info("Fetching all locations");
        var locations = locationRepository.findAll();
        return locations
                .stream()
                .map(LocationDto::fromLocation)
                .toList();
    }

    public LocationDto getLocationById(Long id) {
        logger.info("Fetching location by id: {}", id);
        return locationRepository.findById(id)
                .map(LocationDto::fromLocation)
                .orElse(null);
    }

    public List<ProductDto> getProductAtGivenLocation(Long id) {
        logger.info("Fetching product at given location {}", id);
        return locationRepository
                .findProductsAtGivenLocation(id)
                .stream()
                .map(ProductDto::fromProduct)
                .toList();
    }

}
