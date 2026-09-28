package com.community.filmjury.repository;

import com.community.filmjury.entity.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreCardRepository
        extends JpaRepository<ScoreCard, Integer> {

    boolean existsByJudge_IdAndEntry_Id(
            Integer judgeId,
            Integer entryId
    );

    List<ScoreCard> findByEntry_Id(
            Integer entryId
    );
}