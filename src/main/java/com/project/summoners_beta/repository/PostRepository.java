package com.project.summoners_beta.repository;

import com.project.summoners_beta.model.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Optional<List<PostEntity>> findAllByOrderByIdDesc();
}
