package com.group3.bikehub.repository;

import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);

    Optional<Brand> findById(Long id); 
}
