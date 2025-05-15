package fun.example.parcel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.example.parcel.db.entity.UserEntity;

public  interface UserRepository extends JpaRepository<UserEntity, Long> {
    
}
