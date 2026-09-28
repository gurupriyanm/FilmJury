package com.community.filmjury.controller;

import com.community.filmjury.entity.EntryRanking;
import com.community.filmjury.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(
            RankingService rankingService) {

        this.rankingService = rankingService;
    }

    // =========================================================
    // GET COMPLETE RANKING
    // =========================================================

    @GetMapping
    public ResponseEntity<List<EntryRanking>> getRanking() {

        return ResponseEntity.ok(
                rankingService.getRanking()
        );
    }

    // =========================================================
    // GET AVERAGE FOR ONE ENTRY
    // =========================================================

    @GetMapping("/entry/{entryId}")
    public ResponseEntity<Double> getEntryAverage(
            @PathVariable Integer entryId) {

        return ResponseEntity.ok(
                rankingService.calculateEntryAverage(
                        entryId
                )
        );
    }
}