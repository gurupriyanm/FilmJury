package com.community.filmjury.service;

import com.community.filmjury.entity.Criterion;
import com.community.filmjury.entity.CriterionScore;
import com.community.filmjury.entity.Entry;
import com.community.filmjury.entity.Judge;
import com.community.filmjury.entity.ScoreCard;
import com.community.filmjury.repository.CriterionRepository;
import com.community.filmjury.repository.EntryRepository;
import com.community.filmjury.repository.JudgeRepository;
import com.community.filmjury.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScoreCardService {

    private final ScoreCardRepository scoreCardRepository;
    private final JudgeRepository judgeRepository;
    private final EntryRepository entryRepository;
    private final CriterionRepository criterionRepository;

    public ScoreCardService(
            ScoreCardRepository scoreCardRepository,
            JudgeRepository judgeRepository,
            EntryRepository entryRepository,
            CriterionRepository criterionRepository) {

        this.scoreCardRepository = scoreCardRepository;
        this.judgeRepository = judgeRepository;
        this.entryRepository = entryRepository;
        this.criterionRepository = criterionRepository;
    }

    // =========================================================
    // CREATE SCORECARD
    // =========================================================

    public ScoreCard createScoreCard(
            Integer judgeId,
            Integer entryId,
            List<CriterionScore> scores) {

        // -----------------------------------------------------
        // 1. Check whether Judge exists
        // -----------------------------------------------------

        Judge judge = judgeRepository.findById(judgeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Judge not found with ID: " + judgeId
                        )
                );

        // -----------------------------------------------------
        // 2. Check whether Entry exists
        // -----------------------------------------------------

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Entry not found with ID: " + entryId
                        )
                );

        // -----------------------------------------------------
        // 3. Check Judge-Entry assignment
        // -----------------------------------------------------

        boolean assigned = judge.getAssignedEntries()
                .stream()
                .anyMatch(e ->
                        e.getId().equals(entryId)
                );

        if (!assigned) {
            throw new RuntimeException(
                    "Judge " + judgeId +
                            " is not assigned to Entry " +
                            entryId
            );
        }

        // -----------------------------------------------------
        // 4. Check duplicate scorecard
        // -----------------------------------------------------

        if (scoreCardRepository.existsByJudge_IdAndEntry_Id(
                judgeId,
                entryId)) {

            throw new RuntimeException(
                    "Judge " + judgeId +
                            " has already submitted a scorecard for Entry " +
                            entryId
            );
        }

        // -----------------------------------------------------
        // 5. Check whether scores were supplied
        // -----------------------------------------------------

        if (scores == null || scores.isEmpty()) {

            throw new RuntimeException(
                    "At least one criterion score is required"
            );
        }

        // -----------------------------------------------------
        // 6. Get all criteria
        // -----------------------------------------------------

        List<Criterion> allCriteria =
                criterionRepository.findAll();

        if (allCriteria.isEmpty()) {

            throw new RuntimeException(
                    "No criteria have been configured"
            );
        }

        // -----------------------------------------------------
        // 7. Check that ALL criteria were scored
        // -----------------------------------------------------

        if (scores.size() != allCriteria.size()) {

            throw new RuntimeException(
                    "All " +
                            allCriteria.size() +
                            " criteria must be scored"
            );
        }

        // -----------------------------------------------------
        // 8. Prevent duplicate criterion IDs
        // -----------------------------------------------------

        Set<Integer> criterionIds =
                new HashSet<>();

        for (CriterionScore criterionScore : scores) {

            if (criterionScore.getCriterionId() == null) {

                throw new RuntimeException(
                        "Criterion ID cannot be null"
                );
            }

            if (!criterionIds.add(
                    criterionScore.getCriterionId())) {

                throw new RuntimeException(
                        "Duplicate criterion ID: " +
                                criterionScore.getCriterionId()
                );
            }
        }

        // -----------------------------------------------------
        // 9. Make sure every existing criterion is included
        // -----------------------------------------------------

        for (Criterion criterion : allCriteria) {

            if (!criterionIds.contains(
                    criterion.getId())) {

                throw new RuntimeException(
                        "Missing score for criterion: " +
                                criterion.getName()
                );
            }
        }

        // -----------------------------------------------------
        // 10. Validate each individual score
        // -----------------------------------------------------

        for (CriterionScore criterionScore : scores) {

            Integer criterionId =
                    criterionScore.getCriterionId();

            Integer score =
                    criterionScore.getScore();

            Criterion criterion =
                    criterionRepository.findById(criterionId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Criterion not found with ID: "
                                                    + criterionId
                                    )
                            );

            // Score cannot be null

            if (score == null) {

                throw new RuntimeException(
                        "Score cannot be null for criterion: " +
                                criterion.getName()
                );
            }

            // Score cannot be negative

            if (score < 0) {

                throw new RuntimeException(
                        "Score cannot be negative for criterion: " +
                                criterion.getName()
                );
            }

            // Score cannot exceed maximum

            if (score > criterion.getMaxScore()) {

                throw new RuntimeException(
                        "Score for " +
                                criterion.getName() +
                                " cannot exceed " +
                                criterion.getMaxScore()
                );
            }
        }

        // -----------------------------------------------------
        // 11. Create ScoreCard
        // -----------------------------------------------------

        ScoreCard scoreCard = new ScoreCard();

        scoreCard.setJudge(judge);
        scoreCard.setEntry(entry);
        scoreCard.setScores(scores);

        return scoreCardRepository.save(scoreCard);
    }

    // =========================================================
    // GET ALL SCORECARDS
    // =========================================================

    public List<ScoreCard> getAllScoreCards() {

        return scoreCardRepository.findAll();
    }

    // =========================================================
    // GET SCORECARD BY ID
    // =========================================================

    public ScoreCard getScoreCardById(Integer id) {

        return scoreCardRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "ScoreCard not found with ID: " + id
                        )
                );
    }

    // =========================================================
    // CALCULATE SCORECARD AVERAGE
    // =========================================================

    public double calculateScoreCardAverage(
            Integer scoreCardId) {

        ScoreCard scoreCard =
                getScoreCardById(scoreCardId);

        List<CriterionScore> scores =
                scoreCard.getScores();

        if (scores == null || scores.isEmpty()) {

            throw new RuntimeException(
                    "ScoreCard has no scores"
            );
        }

        double total = 0;

        for (CriterionScore criterionScore : scores) {

            total += criterionScore.getScore();
        }

        return total / scores.size();
    }
}