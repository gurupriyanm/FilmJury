package com.community.filmjury.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class CriterionScore {

    private Integer criterionId;

    private Integer score;

    public CriterionScore() {
    }

    public CriterionScore(Integer criterionId, Integer score) {
        this.criterionId = criterionId;
        this.score = score;
    }

    public Integer getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Integer criterionId) {
        this.criterionId = criterionId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}