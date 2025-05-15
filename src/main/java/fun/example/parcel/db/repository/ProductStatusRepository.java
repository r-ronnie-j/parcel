package fun.example.parcel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fun.example.parcel.db.entity.ProductStatusEntity;

public interface ProductStatusRepository extends JpaRepository<ProductStatusEntity, Long> {

    @Query("SELECT ps FROM ProductStatusEntity ps JOIN ps.transit t JOIN t.product p WHERE p.id = :productId")
    List<ProductStatusEntity> findAllByProductId(@Param("productId") Long productId);

}
