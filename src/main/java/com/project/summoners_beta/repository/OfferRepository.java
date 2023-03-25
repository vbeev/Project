package com.project.summoners_beta.repository;

import com.project.summoners_beta.model.entities.OfferEntity;
import com.project.summoners_beta.model.enums.OfferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {
    Optional<List<OfferEntity>> findAllByOfferTypeAndUsernameNot(OfferType offerType, String username);
    Optional<List<OfferEntity>> findAllByUsername(String username);
}
