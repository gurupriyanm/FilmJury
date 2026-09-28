package com.community.filmjury.controller;

import com.community.filmjury.entity.Criterion;
import com.community.filmjury.service.CriterionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criteria")
public class CriterionController {

    private final CriterionService criterionService;

    public CriterionController(CriterionService criterionService) {
        this.criterionService = criterionService;
    }

    // CREATE

    @PostMapping
    public ResponseEntity<Criterion> createCriterion(
            @RequestBody Criterion criterion) {

        return ResponseEntity.ok(
                criterionService.createCriterion(criterion)
        );
    }

    // GET ALL

    @GetMapping
    public ResponseEntity<List<Criterion>> getAllCriteria() {

        return ResponseEntity.ok(
                criterionService.getAllCriteria()
        );
    }

    // GET BY ID

    @GetMapping("/{id}")
    public ResponseEntity<Criterion> getCriterionById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                criterionService.getCriterionById(id)
        );
    }

    // UPDATE

    @PutMapping("/{id}")
    public ResponseEntity<Criterion> updateCriterion(
            @PathVariable Integer id,
            @RequestBody Criterion criterion) {

        return ResponseEntity.ok(
                criterionService.updateCriterion(
                        id,
                        criterion
                )
        );
    }

    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCriterion(
            @PathVariable Integer id) {

        criterionService.deleteCriterion(id);

        return ResponseEntity.ok(
                "Criterion deleted successfully"
        );
    }
}