package fun.example.parcel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fun.example.parcel.db.entity.LocationEntity;
import fun.example.parcel.db.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT l FROM LocationEntity l " +
            "JOIN l.transits t " +
            "JOIN t.product p " +
            "JOIN t.productStatus ps " +
            "WHERE p.id = :productId ORDER BY ps.createdAt DESC LIMIT 1")
    LocationEntity findCurrentLocationOfProduct(@Param("productId") Long productId);

}
