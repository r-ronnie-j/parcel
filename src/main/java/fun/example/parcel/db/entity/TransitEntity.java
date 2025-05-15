package fun.example.parcel.db.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transit")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TransitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    ProductEntity product;

    @ManyToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    LocationEntity location;

    @OneToMany(mappedBy = "transit", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<ProductStatusEntity> productStatus;

}
