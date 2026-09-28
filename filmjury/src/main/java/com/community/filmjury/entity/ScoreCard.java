package com.community.filmjury.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "score_cards",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_judge_entry",
                        columnNames = {"judge_id", "entry_id"}
                )
        }
)
public class ScoreCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(
            name = "judge_id",
            nullable = false
    )
    private Judge judge;

    @ManyToOne
    @JoinColumn(
            name = "entry_id",
            nullable = false
    )
    private Entry entry;

    @ElementCollection
    @CollectionTable(
            name = "criterion_scores",
            joinColumns = @JoinColumn(
                    name = "score_card_id"
            )
    )
    private List<CriterionScore> scores = new ArrayList<>();

    public ScoreCard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Judge getJudge() {
        return judge;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public List<CriterionScore> getScores() {
        return scores;
    }

    public void setScores(List<CriterionScore> scores) {
        this.scores = scores;
    }
}