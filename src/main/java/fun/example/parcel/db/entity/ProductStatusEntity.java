package fun.example.parcel.db.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_status")
public class ProductStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;



    @Column(name="created_at",updatable = false,insertable = false)
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Out_For_Delivery', 'Picked_Up', 'Delivered') DEFAULT 'USER'")
    private DeliveryStatus status;

    @ManyToOne()
    @JoinColumn(name = "transit_id", referencedColumnName = "id")
    TransitEntity transit;
}
