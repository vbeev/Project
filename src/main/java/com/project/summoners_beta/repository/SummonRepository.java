package com.project.summoners_beta.repository;

import com.project.summoners_beta.model.entities.SummonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummonRepository extends JpaRepository<SummonEntity, Long> {
    Optional<SummonEntity> findByUserId(Long userId);
    Optional<List<SummonEntity>> findAllByUser_Username(String username);

    long count();
}
