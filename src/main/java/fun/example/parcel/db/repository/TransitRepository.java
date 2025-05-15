package fun.example.parcel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.example.parcel.db.entity.TransitEntity;

public interface TransitRepository extends JpaRepository<TransitEntity, Long> {
    
}
