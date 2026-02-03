package com.group3.bikehub.repository;

import com.group3.bikehub.entity.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ListingImagineRepository extends JpaRepository<ListingImage, UUID> {

}
