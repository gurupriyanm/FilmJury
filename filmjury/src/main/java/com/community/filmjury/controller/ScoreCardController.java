package com.community.filmjury.controller;

import com.community.filmjury.entity.CriterionScore;
import com.community.filmjury.entity.ScoreCard;
import com.community.filmjury.service.ScoreCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scorecards")
public class ScoreCardController {

    private final ScoreCardService scoreCardService;

    public ScoreCardController(
            ScoreCardService scoreCardService) {

        this.scoreCardService = scoreCardService;
    }

    // =========================================================
    // CREATE SCORECARD
    // =========================================================

    @PostMapping
    public ResponseEntity<ScoreCard> createScoreCard(
            @RequestParam Integer judgeId,
            @RequestParam Integer entryId,
            @RequestBody List<CriterionScore> scores) {

        ScoreCard scoreCard =
                scoreCardService.createScoreCard(
                        judgeId,
                        entryId,
                        scores
                );

        return ResponseEntity.ok(scoreCard);
    }

    // =========================================================
    // GET ALL SCORECARDS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ScoreCard>> getAllScoreCards() {

        return ResponseEntity.ok(
                scoreCardService.getAllScoreCards()
        );
    }

    // =========================================================
    // GET SCORECARD BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ScoreCard> getScoreCardById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                scoreCardService.getScoreCardById(id)
        );
    }

    // =========================================================
    // GET SCORECARD AVERAGE
    // =========================================================

    @GetMapping("/{id}/average")
    public ResponseEntity<Double> getScoreCardAverage(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                scoreCardService.calculateScoreCardAverage(id)
        );
    }
}