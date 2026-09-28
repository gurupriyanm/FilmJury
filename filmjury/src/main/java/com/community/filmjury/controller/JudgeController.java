package com.community.filmjury.controller;

import com.community.filmjury.entity.Judge;
import com.community.filmjury.service.JudgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
public class JudgeController {

    private final JudgeService judgeService;

    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    // ---------------- CREATE JUDGE ----------------

    @PostMapping
    public ResponseEntity<Judge> createJudge(
            @RequestBody Judge judge) {

        Judge savedJudge = judgeService.createJudge(judge);

        return ResponseEntity.ok(savedJudge);
    }

    // ---------------- GET ALL JUDGES ----------------

    @GetMapping
    public ResponseEntity<List<Judge>> getAllJudges() {

        return ResponseEntity.ok(
                judgeService.getAllJudges()
        );
    }

    // ---------------- GET JUDGE BY ID ----------------

    @GetMapping("/{id}")
    public ResponseEntity<Judge> getJudgeById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                judgeService.getJudgeById(id)
        );
    }

    // ---------------- UPDATE JUDGE ----------------

    @PutMapping("/{id}")
    public ResponseEntity<Judge> updateJudge(
            @PathVariable Integer id,
            @RequestBody Judge judge) {

        return ResponseEntity.ok(
                judgeService.updateJudge(id, judge)
        );
    }

    // ---------------- DELETE JUDGE ----------------

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJudge(
            @PathVariable Integer id) {

        judgeService.deleteJudge(id);

        return ResponseEntity.ok(
                "Judge deleted successfully"
        );
    }

    // ---------------- ASSIGN ENTRY ----------------

    @PutMapping("/{judgeId}/entries/{entryId}")
    public ResponseEntity<Judge> assignEntry(
            @PathVariable Integer judgeId,
            @PathVariable Integer entryId) {

        return ResponseEntity.ok(
                judgeService.assignEntryToJudge(
                        judgeId,
                        entryId
                )
        );
    }

    // ---------------- REMOVE ENTRY ----------------

    @DeleteMapping("/{judgeId}/entries/{entryId}")
    public ResponseEntity<Judge> removeEntry(
            @PathVariable Integer judgeId,
            @PathVariable Integer entryId) {

        return ResponseEntity.ok(
                judgeService.removeEntryFromJudge(
                        judgeId,
                        entryId
                )
        );
    }
}