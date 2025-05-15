package fun.example.parcel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fun.example.parcel.db.entity.LocationEntity;
import fun.example.parcel.db.entity.ProductEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    @Query("SELECT DISTINCT p FROM ProductEntity p " +
            "JOIN p.transits t " +
            "JOIN t.location l " +
            "WHERE l.id = :locationId " +
            "AND (SELECT ps.status FROM ProductStatusEntity ps " +
            "      WHERE ps.transit = t " +
            "      ORDER BY ps.createdAt DESC LIMIT 1) = 'Out_For_Delivery'")
    List<ProductEntity> findProductsAtGivenLocation(@Param("locationId") Long locationId);

}
