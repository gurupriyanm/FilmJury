package com.community.filmjury.entity;

public class EntryRanking {

    private Integer entryId;

    private String title;

    private String genre;

    private double average;

    private int judgeCount;

    public EntryRanking(
            Integer entryId,
            String title,
            String genre,
            double average,
            int judgeCount) {

        this.entryId = entryId;
        this.title = title;
        this.genre = genre;
        this.average = average;
        this.judgeCount = judgeCount;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public double getAverage() {
        return average;
    }

    public int getJudgeCount() {
        return judgeCount;
    }
}