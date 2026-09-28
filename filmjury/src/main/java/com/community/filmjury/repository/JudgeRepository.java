package com.community.filmjury.repository;

import com.community.filmjury.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeRepository extends JpaRepository<Judge, Integer> {
}