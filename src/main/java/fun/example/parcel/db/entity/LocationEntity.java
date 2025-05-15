package fun.example.parcel.db.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="location")
@Builder
@AllArgsConstructor 
@NoArgsConstructor
@Data
public class LocationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="name")
    String name;

    @Column(name="description")
    String description;

    @Column(name="email",unique = true,nullable = false)
    String email;

    @Column(name="created_at",updatable = false,insertable = false)
    LocalDate createdAt;

    @OneToMany(mappedBy = "location")
    List<TransitEntity> transits;
}
