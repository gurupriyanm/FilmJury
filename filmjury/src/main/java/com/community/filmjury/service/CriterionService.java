package com.community.filmjury.service;

import com.community.filmjury.entity.Criterion;
import com.community.filmjury.repository.CriterionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriterionService {

    private final CriterionRepository criterionRepository;

    public CriterionService(CriterionRepository criterionRepository) {
        this.criterionRepository = criterionRepository;
    }

    // CREATE

    public Criterion createCriterion(Criterion criterion) {
        return criterionRepository.save(criterion);
    }

    // GET ALL

    public List<Criterion> getAllCriteria() {
        return criterionRepository.findAll();
    }

    // GET BY ID

    public Criterion getCriterionById(Integer id) {

        return criterionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Criterion not found with ID: " + id
                        )
                );
    }

    // UPDATE

    public Criterion updateCriterion(
            Integer id,
            Criterion updatedCriterion) {

        Criterion existingCriterion = getCriterionById(id);

        existingCriterion.setName(
                updatedCriterion.getName()
        );

        existingCriterion.setDescription(
                updatedCriterion.getDescription()
        );

        existingCriterion.setMaxScore(
                updatedCriterion.getMaxScore()
        );

        return criterionRepository.save(existingCriterion);
    }

    // DELETE

    public void deleteCriterion(Integer id) {

        if (!criterionRepository.existsById(id)) {
            throw new RuntimeException(
                    "Criterion not found with ID: " + id
            );
        }

        criterionRepository.deleteById(id);
    }
}