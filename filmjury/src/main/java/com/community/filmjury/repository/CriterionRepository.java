package com.community.filmjury.repository;

import com.community.filmjury.entity.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriterionRepository extends JpaRepository<Criterion, Integer> {
}