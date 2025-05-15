package fun.example.parcel.db.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "price")
    Double price;

    @Column(name = "created_at", updatable = false, insertable = false)
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<TransitEntity> transits;

    public ProductEntity(String name, String description, Double price, Long userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.user = new UserEntity();
        this.user.id = userId;
        this.transits = new ArrayList<>();
    }
}
