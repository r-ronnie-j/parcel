package fun.example.parcel.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fun.example.parcel.config.SecurityConfig;
import fun.example.parcel.db.dto.LocationDto;
import fun.example.parcel.db.dto.ProductDto;
import fun.example.parcel.db.dto.ProductStatusDto;
import fun.example.parcel.db.dto.TransitDto;
import fun.example.parcel.db.entity.DeliveryStatus;
import fun.example.parcel.service.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(LocationController.class)
@Import(SecurityConfig.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    @Test
    void testGetLocationById_Success() throws Exception {
        LocationDto dto = new LocationDto(
                1L,
                "Delhi",
                "Testing at delhi",
                "delhi@gmail.com",
                null
        );

        when(locationService.getLocationById(1L)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/location/1").with(request -> {
                    request.setSecure(true);  // ← Simulate HTTPS
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Delhi"));
    }

    @Test
    void testGetAllLocation_Success() throws Exception {
        LocationDto dto = new LocationDto(
                1L,
                "Delhi",
                "Testing at delhi",
                "delhi@gmail.com",
                null
        );

        when(locationService.getAllLocations()).thenReturn(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders.get("/location/allLocation").with(request -> {
                    request.setSecure(true);
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Delhi"));
    }

    @Test
    void testGetProductsAtLocation() throws Exception {
        LocationDto dto = new LocationDto(
                1L,
                "Delhi",
                "Testing at delhi",
                "delhi@gmail.com",
                null
        );

        List<ProductDto> products = List.of(
                new ProductDto(
                        1L,
                        "First Product",
                        "This is first product",
                        11.1,
                        LocalDateTime.now(),
                        List.of(
                                new TransitDto(1L, dto, List.of(
                                        new ProductStatusDto(1L, LocalDateTime.now(), DeliveryStatus.Delivered)
                                ))
                        )
                )
        );


        when(locationService.getProductAtGivenLocation(1L)).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/location/products/1").with(request -> {
                    request.setSecure(true);  // ← Simulate HTTPS
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("First Product"))
                .andExpect(jsonPath("$.data[0].transits[0].productStatus[0].status").value(DeliveryStatus.Delivered.name()))

        ;
    }

    @Test
    void testAddLocation_Success() throws Exception {
        LocationDto dto = new LocationDto(
                1L,
                "Delhi",
                "Testing at delhi",
                "delhi@gmail.com",
                null
        );


        when(locationService.addLocation(Mockito.any())).thenReturn(dto);

        String requestBody = """
                    {
                      "name": "Delhi",
                      "email": "delhi@example.com"
                    }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/location/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody).with(req -> {
                            req.setSecure(true);  // ← Simulate HTTPS
                            return req;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Delhi"));
    }
}
