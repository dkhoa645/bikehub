package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Favorite;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);

    void deleteByIdAndUser(Long id, User user);

    void deleteByListingAndUser(Listing listing, User user);

    Boolean existsByUserAndListing(User user, Listing listing);
}
