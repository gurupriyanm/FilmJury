package com.community.filmjury.service;

import com.community.filmjury.entity.CriterionScore;
import com.community.filmjury.entity.Entry;
import com.community.filmjury.entity.EntryRanking;
import com.community.filmjury.entity.ScoreCard;
import com.community.filmjury.repository.EntryRepository;
import com.community.filmjury.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {

    private final EntryRepository entryRepository;
    private final ScoreCardRepository scoreCardRepository;

    public RankingService(
            EntryRepository entryRepository,
            ScoreCardRepository scoreCardRepository) {

        this.entryRepository = entryRepository;
        this.scoreCardRepository = scoreCardRepository;
    }

    // =========================================================
    // CALCULATE ONE SCORECARD AVERAGE
    // =========================================================

    private double calculateScoreCardAverage(
            ScoreCard scoreCard) {

        List<CriterionScore> scores =
                scoreCard.getScores();

        if (scores == null || scores.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (CriterionScore score : scores) {

            total += score.getScore();
        }

        return total / scores.size();
    }

    // =========================================================
    // CALCULATE FINAL AVERAGE FOR ONE ENTRY
    // =========================================================

    public double calculateEntryAverage(
            Integer entryId) {

        // Check Entry exists

        entryRepository.findById(entryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Entry not found with ID: " +
                                        entryId
                        )
                );

        // Get submitted scorecards for this entry

        List<ScoreCard> scoreCards =
                scoreCardRepository.findByEntry_Id(
                        entryId
                );

        if (scoreCards.isEmpty()) {

            throw new RuntimeException(
                    "No submitted scorecards for Entry " +
                            entryId
            );
        }

        double total = 0;

        for (ScoreCard scoreCard : scoreCards) {

            total += calculateScoreCardAverage(
                    scoreCard
            );
        }

        return total / scoreCards.size();
    }

    // =========================================================
    // GET COMPLETE RANKING
    // =========================================================

    public List<EntryRanking> getRanking() {

        List<Entry> entries =
                entryRepository.findAll();

        List<EntryRanking> rankings =
                new ArrayList<>();

        for (Entry entry : entries) {

            List<ScoreCard> scoreCards =
                    scoreCardRepository.findByEntry_Id(
                            Math.toIntExact(entry.getId())
                    );

            // Don't rank entries with no submitted scores

            if (scoreCards.isEmpty()) {
                continue;
            }

            double total = 0;

            for (ScoreCard scoreCard : scoreCards) {

                total += calculateScoreCardAverage(
                        scoreCard
                );
            }

            double average =
                    total / scoreCards.size();

            EntryRanking ranking =
                    new EntryRanking(
                            Math.toIntExact(entry.getId()),
                            entry.getTitle(),
                            entry.getGenre(),
                            average,
                            scoreCards.size()
                    );

            rankings.add(ranking);
        }

        // Highest average first

        rankings.sort(
                Comparator.comparing(
                        EntryRanking::getAverage
                ).reversed()
        );

        return rankings;
    }
}