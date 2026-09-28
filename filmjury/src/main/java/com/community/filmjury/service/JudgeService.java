package com.community.filmjury.service;

import com.community.filmjury.entity.Entry;
import com.community.filmjury.entity.Judge;
import com.community.filmjury.repository.EntryRepository;
import com.community.filmjury.repository.JudgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JudgeService {

    private final JudgeRepository judgeRepository;
    private final EntryRepository entryRepository;

    public JudgeService(
            JudgeRepository judgeRepository,
            EntryRepository entryRepository) {

        this.judgeRepository = judgeRepository;
        this.entryRepository = entryRepository;
    }

    // ---------------- CREATE ----------------

    public Judge createJudge(Judge judge) {
        return judgeRepository.save(judge);
    }

    // ---------------- READ ALL ----------------

    public List<Judge> getAllJudges() {
        return judgeRepository.findAll();
    }

    // ---------------- READ BY ID ----------------

    public Judge getJudgeById(Integer id) {

        Optional<Judge> judge = judgeRepository.findById(id);

        if (judge.isPresent()) {
            return judge.get();
        }

        throw new RuntimeException(
                "Judge not found with ID: " + id
        );
    }

    // ---------------- UPDATE ----------------

    public Judge updateJudge(
            Integer id,
            Judge updatedJudge) {

        Judge existingJudge = getJudgeById(id);

        existingJudge.setName(updatedJudge.getName());
        existingJudge.setEmail(updatedJudge.getEmail());

        return judgeRepository.save(existingJudge);
    }

    // ---------------- DELETE ----------------

    public void deleteJudge(Integer id) {

        if (!judgeRepository.existsById(id)) {
            throw new RuntimeException(
                    "Judge not found with ID: " + id
            );
        }

        judgeRepository.deleteById(id);
    }

    // ---------------- ASSIGN ENTRY ----------------

    public Judge assignEntryToJudge(
            Integer judgeId,
            Integer entryId) {

        Judge judge = getJudgeById(judgeId);

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Entry not found with ID: " + entryId
                        )
                );

        judge.getAssignedEntries().add(entry);

        return judgeRepository.save(judge);
    }

    // ---------------- REMOVE ENTRY ----------------

    public Judge removeEntryFromJudge(
            Integer judgeId,
            Integer entryId) {

        Judge judge = getJudgeById(judgeId);

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Entry not found with ID: " + entryId
                        )
                );

        judge.getAssignedEntries().remove(entry);

        return judgeRepository.save(judge);
    }
}